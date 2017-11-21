<template>
  <div>
    <div class="chatContent"></div>
    <textarea id="chatBox" cols="30" rows="10"></textarea>
    <button type="button" onclick="submit()">提交</button>
  </div>
</template>

<script>
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
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
