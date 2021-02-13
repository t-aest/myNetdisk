import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/components/Main'
import Login from '../views/Login'

Vue.use(Router)

const VueRouterPush = Router.prototype.push
Router.prototype.push = function push (to) {
  return VueRouterPush.call(this, to).catch(err => err)
}
export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [{
    path: '*',
    redirect: '/login'
  }, {
    path: '',
    redirect: '/login'
  }, {
    path: '/login',
    component: Login
  },
  {
    path: '/main',
    name: 'Main',
    component: Main
  }
  ]
})
