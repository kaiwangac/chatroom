<template>
  <div>
    <div class="chatContent">
      <chat-bubble/>
    </div>
    <textarea id="chatBox" cols="30" rows="10"></textarea>
    <button type="button" onclick="submit()">提交</button>
  </div>
</template>

<script>
import ChatBubble from './ChatBubble.vue'

export default {
  name: 'ChatRoom',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  },
  methods: {
    submit: function () {
      // fetch("http://localhost/messages", {method: "POST"}).then(function (response) {
      console.log('submit')
      // })
    }
  },
  components: {ChatBubble}
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
