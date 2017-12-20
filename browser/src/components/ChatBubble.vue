<template>
  <div class = "bubble bubble_primary">
      <div class = "bubble_cont">
          <div class = "plain">{{msg}}</div>
      </div>
  </div>
</template>

<script>
export default {
  name: 'ChatBubble',
  data () {
    return {
      msg: 'hello world'
    }
  }
}
</script>


<style scoped>
.bubble {
    max-width: 500px;
    min-height: 1em;
    display: inline-block;
    vertical-align: top;
    position: relative;
    text-align: left;
    font-size: 14px;
    border-radius: 3px;
    -moz-border-radius: 3px;
    -webkit-border-radius: 3px;
    margin: 0 10px;
}
.bubble:after, .bubble:before {
    position: absolute;
    top: 14px;
    border: 6px solid transparent;
    content: " ";
}
.bubble.bubble_primary {
    background-color: #b2e281;
}
.bubble_cont {
    word-wrap: break-word;
    word-break: break-all;
    min-height: 25px;
}
.bubble_cont .plain {
    padding: 9px 13px;
}
</style>

