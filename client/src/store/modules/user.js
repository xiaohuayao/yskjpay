import {
  login,
  logout,
  getInfo
} from '@/api/user'
import {
  getToken,
  setToken,
  removeToken
} from '@/utils/auth'
import {
  resetRouter,
  constantRoutes
} from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: ''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROUTERS: (state, routers) => {
    state.addRouters = routers
    const a = constantRoutes.concat(routers)
    state.routers = a
  }
}

const actions = {
  // user login
  login({
    commit
  }, userInfo) {
    // debugger;
    const {
      userName,
      password,
	  code
    } = userInfo
    return new Promise((resolve, reject) => {
      login({
        userName: userName.trim(),
        password: password,
		code: code
      }).then(response => {
        const {
          data
        } = response
        console.log(data)
        commit('SET_TOKEN', data)
        setToken(data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfos({
    commit,
    state
  }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const {
          data
        } = response

        if (!data) {
          reject('获取信息失败，请重新登陆')
        }
        commit('SET_NAME', data.userEntity.realName ? data.userEntity.realName : data.userEntity.loginName)

        resolve(data)
      }).catch(error => {
        reject(error)
      })
      resolve()
    })
  },

  // user logout
  logout({
    commit,
    state
  }) {
    // debugger;
    return new Promise((resolve, reject) => {
      // debugger;
      removeToken() // must remove  token  first
      resetRouter()
      commit('RESET_STATE')
      resolve()
    })
  },

  // remove token
  resetToken({
    commit
  }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
