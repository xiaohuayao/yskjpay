import { constantRoutes } from '@/router'
import router from '@/router'
import Layout from '../../layout/index' // Layout 是架构组件，不在后台返回，在文件里单独引入
import { getMenu } from '@/api/user'
import { getToken } from '@/utils/auth'
import store from '@/store'
// import load from './load.js';
import { loadView } from './load.js';


// eslint-disable-next-line no-unused-vars
function filterAsyncRouter (asyncRouterMap) { // 遍历后台传来的路由字符串，转换为组件对象
  const accessedRouters = asyncRouterMap.filter(route => {
    if (route.component) {
      if (route.component === 'Layout') { // Layout组件特殊处理
        route.component = Layout
      } else {
        let a = route.component;
        route.component = loadView(a)
      }
    }
    if (route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children)
    }
    return true
  })
  return accessedRouters
}

function transMenu (asyncRouters, data) {
  for (var item of data) {
    const menu = {
      path: item.route,
      name: item.name,
      component: item.component,
      meta: { title: item.title, icon: item.icon }
    }

    if (item.children && item.children.length > 0) {
      menu.children = []
      transMenu(menu.children, item.children)
    }
    asyncRouters.push(menu)
  }
}
const permission = {
  state: {
    routers: constantRoutes,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      const a = constantRoutes.concat(routers)
      state.routers = a
    }
  },
  actions: {
    generateRoutes ({ commit }) {
      return new Promise(resolve => {
        getMenu(getToken).then(response => {
          var routes = response.menuList
          // routes.push({ 'path': '*', 'redirect': '/404', 'hidden': true })
          console.log(response.menuList)
          const constRoutes = []
          transMenu(constRoutes, routes)
          // constRoutes.push({ 'path': '*', 'redirect': '/404', 'hidden': true })
          var asyncRoutes = filterAsyncRouter(constRoutes)
          // asyncRoutes.push({ 'path': '*', 'redirect': '/404', 'hidden': true })
          commit('SET_ROUTERS', asyncRoutes)
          router.options.routes = router.options.routes.concat(store.getters.addRouters)
          router.addRoutes(store.getters.addRouters)
          resolve()
        })
      })
    }
  }
}

export default permission
