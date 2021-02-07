<template>
  <div class="login-main">
    <h1 style="text-align: center;padding-bottom: 50px">myNetdisk</h1>
    <el-form  ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item  label="账户名" prop="name">
          <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
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
        <el-button type="primary" @click="submitForm('form')">登录</el-button>
        <el-button @click="resetForm('form')">重置</el-button>
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
        password: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入用户名称', trigger: 'blur' },
          { min: 5, max: 16, message: '长度在 5 到 16 个字符', trigger: 'blur' },
          {
            required: true,
            pattern: /^[a-zA-Z][a-zA-Z0-9_]{4,15}$/,
            message: '用户名不符合规范',
            trigger: 'blur'
          }
        ],
        password: [
          { required: true, message: '请输入用户密码', trigger: 'blur' },
          { min: 8, max: 10, message: '长度在 8 到 10 个字符', trigger: 'blur' },
          {
            required: true,
            pattern: /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{8,10}$/,
            message: '密码不符合规范',
            trigger: 'blur'
          }
        ],
        date1: [
          { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
        ],
        date2: [
          { type: 'date', required: true, message: '请选择时间', trigger: 'change' }
        ],
        type: [
          { type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
        ],
        resource: [
          { required: true, message: '请选择活动资源', trigger: 'change' }
        ],
        desc: [
          { required: true, message: '请填写活动形式', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!')
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
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
