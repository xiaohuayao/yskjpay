import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },


  {
    path: '/user',
    component: Layout,
    redirect: '/user/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/user/index'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user'}
      }
    ]
  },


  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '首页',
      component: () => import('@/views/dashboard/index'),
      meta: {
        title: '首页',
        icon: 'dashboard'
      }
    }]
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  // {
  //   path: '/baseInfo',
  //   component: Layout,
  //   redirect: '/baseinfo/list',
  //   name: 'BaseInfo',
  //   meta: { title: '基础信息管理11', icon: 'example' },
  //   children: [
  //     {
  //       path: 'list',
  //       name: 'UserList',
  //       component: () => import('@/views/baseInfo/person/list'),
  //       meta: { title: '用户管理', icon: 'table' }
  //     },
  //     {
  //       path: 'tree',
  //       name: 'Tree',
  //       component: () => import('@/views/tree/index'),
  //       meta: { title: 'Tree', icon: 'tree' }
  //     }
  //   ]
  // },

  // {
  //   path: '/pay',
  //   component: Layout,
  //   redirect: '/pay',
  //   name: 'pay',
  //   meta: { title: '基础信息', icon: 'example' },
  //   children: [
  //     {
  //       path: '/pay/club/list',
  //       name: '行社信息',
  //       component: () => import('@/views/club/list') ,
  //       meta: {
  //         title: '行社信息',
  //         icon: 'example'
  //       }
  //     },
  //     {
  //       path: '/pay/branch/list',
  //       name: '网点信息',
  //       component: () => import('@/views/branch/list') ,
  //       meta: {
  //         title: '网点信息',
  //         icon: 'example'
  //       }
  //     },
  //   ]
  // },
  // {
  //   path: '/user',
  //   component: Layout,
  //   redirect: '/user',
  //   name: 'user',
  //   meta: { title: '用户管理', icon: 'example' },
  //   children: [
  //     {
  //       path: '/pay/user/list',
  //       name: '用户信息',
  //       component: () => import('@/views/user/list') ,
  //       meta: {
  //         title: '用户信息',
  //         icon: 'example'
  //       }
  //     },
  //   ]
  // },
  // {
  //   path: '/merchant',
  //   component: Layout,
  //   redirect: '/merchant',
  //   name: 'merchant',
  //   meta: { title: '商户管理', icon: 'example' },
		
  //   children: [
	//   {
	//     path: '/pay/merchant/list',
	//     name: '商户列表',
	//     component: () => import('@/views/merchant/list') ,
	//     meta: {
	//       title: '商户列表',
	//       icon: 'example'
	//     }
	//   }
  //   ]
  // },
	// {
	//   path: '/order',
	//   component: Layout,
	//   redirect: '/order',
	//   name: 'order',
	//   meta: { title: '流水报表', icon: 'example' },
	//   children: [
	//   {
	//     path: '/pay/order/merchantStream',
	//     name: '商户流水查询',
	//     component: () => import('@/views/merchantStream/list'),
	//     meta: {
	//       title: '商户流水查询',
	//       icon: 'example'
	//     }
	//   },
	// 	{
	// 	  path: '/pay/order/refund',
	// 	  name: '订单退款查询',
	// 	  component: () => import('@/views/refund/list'),
	// 	  meta: {
	// 	    title: '订单退款查询',
	// 	    icon: 'example'
	// 	  }
	// 	},
	//   {
	//     path: '/pay/order/Summarylist',
	//     name: '商户交易汇总',
	//     component:  () => import('@/views/collect/list'),
	//     meta: {
	//       title: '商户交易汇总',
	//       icon: 'example'
	//     }
	//   },

	//   ]
	// },

]

const createRouter = () => new Router({
  mode: 'history', // require service support
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
