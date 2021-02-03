<template>
  <div class="login-main">
    <h1 style="text-align: center;padding-bottom: 50px">myNetdisk</h1>
    <el-form  ref="form" :model="form" label-width="80px">
      <el-form-item  label="账户名">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="form.password"></el-input>
      </el-form-item>
      <el-form-item label="验证码">
        <el-input style="float: left;width: 200px" v-model="form.code"></el-input>
        <img style="float:left;padding-top: 10px;padding-left: 20px" src="static/img/folder-icon.png"/>
      </el-form-item>
      <el-form-item label="" style="text-align: center">
        <el-checkbox-group v-model="form.type">
          <el-checkbox label="记住密码" name="type"></el-checkbox>
          <el-checkbox label="自动登录" name="type"></el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="" style="text-align: center">
        <el-button type="text">注册账号</el-button>
        <el-button type="text">找回密码</el-button>
      </el-form-item>
      <el-form-item  style="text-align: center">
        <el-button type="primary" @click="onSubmit">登录</el-button>
        <el-button>重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data () {
    return {
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      }
    }
  },
  methods: {
    onSubmit () {
      let self = this
      console.log('submit!')
      self.$router.push('/main')
    }
  }
}
</script>
<style scoped>
.login-main {
  height: 600px;
  width: 400px;
  margin: auto;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}
</style>
