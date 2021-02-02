import axios from 'axios'
import QS from 'qs'

import router from '../router/index' // 配合路由守卫，进行拦截
// import store from './store';    // 多用于token存取

/**
 环境的切换
 */
if (process.env.NODE_ENV === 'development') { // 开发环境
  axios.defaults.baseURL = '/api' // 测试接口
} else if (process.env.NODE_ENV === 'test') { // 测试环境
  axios.defaults.baseURL = '/api' // 测试接口
} else if (process.env.NODE_ENV === 'production') { // 线上环境
  axios.defaults.baseURL = '/api' // 测试接口
}

// 请求超时时间 10S
axios.defaults.timeout = 10000

// 请求头
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'

/**
 * http request 请求拦截，一般用于token 验证
 */

// axios.interceptors.request.use(
//   config => {
//     if(store.state.token){  // 判断token 是否存在，如果存在拼到请求地址后边
//       let reg = new RegExp('"',"g");    //删除请求地址中的 "
//       // 在请求地址中加上 token
//       config.url = `${config.url}?token=${store.state.token}`.replace(reg,"");
//     }
//     return config;
//   },
//   error => {
//     console.log('请求拦截器！');
//     return Promise.reject(error);
//   }
// );

/**
 * http response 响应拦截
 */
axios.interceptors.response.use(
  /**
   * 如果返回的状态码为200，说明接口请求成功，可以正常拿到数据
   * 否则的话抛出错误
   */
  response => {
    if (response.status === 200) {
      return Promise.resolve(response)
    } else {
      return Promise.reject(response)
    }
  },
  /**
   * 服务器状态码不是200的的情况
   * 这里可以跟你们的后台开发人员协商好统一的错误状态码
   * 然后根据返回的状态码进行一些操作，例如登录过期提示，错误提示等等
   * 下面列举几个常见的操作，其他需求可自行扩展
   */
  error => {
    if (error.response.status) {
      switch (error.response.status) {
        /**
         * 401: 未登录
         * 未登录则跳转登录页面，并携带当前页面的路径
         * 在登录成功后返回当前页面，这一步需要在登录页操作。
         */
        case 401:
          router.replace({
            path: '/',
            query: {
              redirect: router.currentRoute.fullPath
            }
          })
          break
        /**
         * 403 token过期
         * 登录过期对用户进行提示
         * 清除本地token和清空vuex中token对象
         * 跳转登录页面
         */
        case 403:
          console.log('登录过期，请重新登录')
          // 清除token
          localStorage.removeItem('token')
          store.commit('loginSuccess', null) // 不太懂的话可不对状态码进行操作
          // 跳转页面，并将要浏览的页面通过fullPath传过去，登录成功后可跳转至需要访问的页面
          setTimeout(() => {
            router.replace({
              path: '/',
              query: {
                redirect: router.currentRoute.fullPath
              }
            })
          }, 1000)
          break
        /**
         * 404请求不存在
         */
        case 404:
          console.log('请求网络请求不存在')
          break
        /**
         * 其他错误，直接抛出错误提示
         */
        default:
          console.log(error.response.data.message)
      }
      return Promise.reject(error.response)
    }
  }
)

/**
 * get方法，对应get请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 */
export function get (url, params) {
  return new Promise((resolve, reject) => {
    axios.get(url + '/' + params)
      .then(res => {
        resolve(res.data)
      }).catch(err => {
        reject(err.data)
      })
  })
}

/**
 * post方法，对应post请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 * @param {String} type [参数类型，默认表单数据，可能为json]
 */
export function post (url, params, type) {
  if (type) { // 传输方式为json
    let headers = {'Content-Type': 'application/json;'}
    if (type == 'DATA' || type == 'data') { // 有文件传输
      headers = {'Content-Type': 'multipart/form-data;'}
    }
    return new Promise((resolve, reject) => {
      axios({
        headers,
        method: 'POST',
        url,
        data: params
      }).then(res => {
        resolve(res.data)
      }).catch(err => {
        reject(err.data)
      })
    })
  } else { // 传输方式为表单
    return new Promise((resolve, reject) => {
      axios.post(url, QS.stringify(params)).then(res => {
        resolve(res.data)
      }).catch(err => {
        reject(err.data)
      })
    })
  }
}

/**
 * put方法，对应put请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 */
export function put (url, params) {
  return new Promise((resolve, reject) => {
    axios.put(url, params)
      .then(res => {
        resolve(res.data)
      })
      .catch(err => {
        reject(err.data)
      })
  })
}

/**
 * $delete，对应delete请求
 */
export function $delete (url, params) {
  return new Promise((resolve, reject) => {
    axios.delete(url + '/' + params, {
    })
      .then(res => {
        resolve(res.data)
      })
      .catch(err => {
        reject(err.data)
      })
  })
}
