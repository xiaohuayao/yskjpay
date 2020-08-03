import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/pay/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/pay/user/getUserInfo',
    method: 'get',
    data: {
      token
    }
  })
}

export function edit(data) {
  return request({
    url: '/pay/user/editPassword',
    method: 'post',
    data
  })
}

export function LogOut({
  commit
}) {
  return new Promise((resolve, reject) => {
    logout().then(res => {
      logOut(commit)
      resolve()
    }).catch(error => {
      logOut(commit)
      reject(error)
    })
  })
}

export function getMenu(token) {
  return request({
    url: '/pay/user/getUserInfo',
    method: 'get',
    data: {
      token
    }
  })
}

export function resetPassword(data) {
  return request({
    url: '/pay/user/resetPassword',
    method: 'post',
    data
  })
}
