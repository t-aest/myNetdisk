<template>
  <div id="app">
    <router-view/>
    <!--  上传组件-->
    <global-uploader></global-uploader>
  </div>

</template>

<script>
import globalUploader from '@/components/GlobalUploader.vue'
export default {
  name: 'App',
  data () {
    return {}
  },
  components: {
    globalUploader
  },
  computed: {},
  created () {
  },
  watch: {}
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>
