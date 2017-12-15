package com.chatroom.jersey;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.ContainerResponse;
import org.glassfish.jersey.server.spi.ContainerResponseWriter;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ResponseWriter implements ContainerResponseWriter {

    private static class VertxOutputStream extends OutputStream {

        final HttpServerResponse response;
        Buffer buffer = Buffer.buffer();
        boolean isClosed;

        private VertxOutputStream(HttpServerResponse response) {
            this.response = response;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(int b) throws IOException {
            checkState();
            buffer.appendByte((byte) b);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(byte[] b) throws IOException {
            checkState();
            buffer.appendBytes(b);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            checkState();
            if (off == 0 && len == b.length) {
                buffer.appendBytes(b);
            } else {
                buffer.appendBytes(Arrays.copyOfRange(b, off, off + len));
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void flush() throws IOException {
            checkState();
            // Only flush to underlying very.x response if the content-length has been set
            if (buffer.length() > 0 && response.headers().contains(HttpHeaders.CONTENT_LENGTH)) {
                response.write(buffer);
                buffer = Buffer.buffer();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException {
            // Write any remaining buffer to the vert.x response
            // Set content-length if not set yet
            if (buffer != null && buffer.length() > 0) {
                if (!response.headers().contains(HttpHeaders.CONTENT_LENGTH)) {
                    response.headers().add(HttpHeaders.CONTENT_LENGTH, String.valueOf(buffer.length()));
                }
                response.write(buffer);
            }
            buffer = null;
            isClosed = true;
        }

        void checkState() {
            if (isClosed) {
                throw new RuntimeException("Stream is closed");
            }
        }
    }

    private static class VertxChunkedOutputStream extends OutputStream {

        private final HttpServerResponse response;
        private boolean isClosed;

        private VertxChunkedOutputStream(HttpServerResponse response) {
            this.response = response;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(int b) throws IOException {
            checkState();
            response.write(Buffer.buffer((byte) b));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(byte[] b) throws IOException {
            checkState();
            response.write(Buffer.buffer(b));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            checkState();
            Buffer buffer = Buffer.buffer();
            if (off == 0 && len == b.length) {
                buffer.appendBytes(b);
            } else {
                buffer.appendBytes(Arrays.copyOfRange(b, off, off + len));
            }
            response.write(buffer);
        }

        @Override
        public void close() throws IOException {
            isClosed = true;
        }

        void checkState() {
            if (isClosed) {
                throw new RuntimeException("Stream is closed");
            }
        }

    }

    private final HttpServerRequest request;
    private final Vertx vertx;

    private long suspendTimerId;
    private TimeoutHandler timeoutHandler;

    public ResponseWriter(
            HttpServerRequest request,
            Vertx vertx) {
        this.request = request;
        this.vertx = vertx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream writeResponseStatusAndHeaders(long contentLength, ContainerResponse responseContext) throws ContainerException {
        HttpServerResponse response = request.response();

        // Write the status
        response.setStatusCode(responseContext.getStatus());
        response.setStatusMessage(responseContext.getStatusInfo().getReasonPhrase());

        // Set the content length header
        if (contentLength != -1) {
            response.putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        }

        for (final Map.Entry<String, List<String>> e : responseContext.getStringHeaders().entrySet()) {
            for (final String value : e.getValue()) {
                response.putHeader(e.getKey(), value);
            }
        }

        // Return output stream based on whether entity is chunked
        if (responseContext.isChunked()) {
            response.setChunked(true);
            return new ResponseWriter.VertxChunkedOutputStream(response);
        } else {
            return new ResponseWriter.VertxOutputStream(response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean suspend(long timeOut, TimeUnit timeUnit, TimeoutHandler timeoutHandler) {
        // Store the timeout handler
        this.timeoutHandler = timeoutHandler;

        // Cancel any existing timer
        if (suspendTimerId != 0) {
            vertx.cancelTimer(suspendTimerId);
            suspendTimerId = 0;
        }

        // If timeout <= 0, then it suspends indefinitely
        if (timeOut <= 0) {
            return true;
        }

        // Get milliseconds
        long ms = timeUnit.toMillis(timeOut);

        // Schedule timeout on the event loop
        this.suspendTimerId = vertx.setTimer(ms, new Handler<Long>() {
            @Override
            public void handle(Long id) {
                if (id == suspendTimerId) {
                    ResponseWriter.this.timeoutHandler.onTimeout(ResponseWriter.this);
                }
            }
        });

        return true;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSuspendTimeout(long timeOut, TimeUnit timeUnit) throws IllegalStateException {

        if (timeoutHandler == null) {
            throw new IllegalStateException("The timeoutHandler is null");
        }

        suspend(timeOut, timeUnit, timeoutHandler);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        request.response().end();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failure(Throwable error) {

        HttpServerResponse response = request.response();

        // Set error status and end
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        response.setStatusCode(status.getStatusCode());
        response.setStatusMessage(status.getReasonPhrase());
        response.end();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean enableResponseBuffering() {
        return false;
    }
}
