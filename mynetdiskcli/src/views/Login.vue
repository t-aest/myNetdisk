<template>
  <div class="login-main">
    <h1 style="text-align: center;padding-bottom: 50px">myNetdisk</h1>
    <el-form  ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item  label="账户名" prop="name">
          <el-input v-model="form.loginName"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="form.password"></el-input>
      </el-form-item>
      <el-form-item label="验证码">
        <el-input style="float: left;width: 200px" v-model="form.imageCode"></el-input>
        <img style="float:left;padding-left: 20px" @click="loadImageCode()" id="image-code" alt="验证码"/>
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
import {Util} from '../assets/js/util'
import $ from 'jquery'
import {apiConfig} from '../request/api'
export default {
  name: 'Login',
  data () {
    return {
      imageCodeToken: '',
      form: {
        name: '',
        password: '',
        loginName: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        imageCode: '',
        remember: true, // 默认勾选记住我
        desc: ''
      },
      rules: {
        loginName: [
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
  mounted () {
    let self = this
    self.loadImageCode()
  },
  methods: {
    submitForm (formName) {
      let self = this
      // 将明文存储到缓存中
      // let passwordShow = _this.user.password;

      // 如果密码是从缓存带出来的，则不需要重新加密
      // let md5 = hex_md5(_this.user.password)
      // let rememberUser = LocalStorage.get(LOCAL_KEY_REMEMBER_USER) || {}
      // if (md5 !== rememberUser.md5) {
      //   _this.user.password = hex_md5(_this.user.password + KEY)
      // }
      self.form.imageCodeToken = self.imageCodeToken
      this.$refs[formName].validate((valid) => {
        if (valid) {
          apiConfig.login(self.form)
            .then(res => {
              if (res.code === 0) {
                this.$router.push('/main')
              } else {
                self.$message.error(res.message)
              }
            }).catch(err => {
              self.$message.error('登录失败：' + err)
            })
        } else {
          console.log('登录验证失败!!')
          return false
        }
      })

      // Loading.show();
      // _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/login', _this.user).then((response) => {
      //   Loading.hide();
      //   let resp = response.data;
      //   if (resp.success) {
      //     console.log("登录成功：", resp.content);
      //     let loginUser = resp.content;
      //     Tool.setLoginUser(resp.content);
      //
      //     // 判断“记住我”
      //     if (_this.remember) {
      //       // 如果勾选记住我，则将用户名密码保存到本地缓存
      //       // 原：这里需要保存密码明文，否则登录时又会再加一层密
      //       // 新：这里保存密码密文，并保存密文md5，用于检测密码是否被重新输入过
      //       let md5 = hex_md5(_this.user.password);
      //       LocalStorage.set(LOCAL_KEY_REMEMBER_USER, {
      //         loginName: loginUser.loginName,
      //         // password: _this.user.passwordShow,
      //         password: _this.user.password,
      //         md5: md5
      //       });
      //     } else {
      //       // 没有勾选“记住我”时，要把本地缓存清空，否则按照mounted的逻辑，下次打开时会自动显示用户名密码
      //       LocalStorage.set(LOCAL_KEY_REMEMBER_USER, null);
      //     }
      //     _this.$router.push("/welcome")
      //   } else {
      //     Toast.warning(resp.message);
      //     _this.user.password = "";
      //     _this.loadImageCode();
      //   }
      // })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    /**
     * 加载图形验证码
     */
    loadImageCode: function () {
      let self = this
      self.imageCodeToken = Util.uuid(8)
      $('#image-code').attr('src', '/api/kaptcha/image-code/' + self.imageCodeToken)
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
