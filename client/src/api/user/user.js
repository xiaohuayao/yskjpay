import request from '@/utils/request'

export function getpageList(data) {
  return request({
    url: '/pay/user/list',
    method: 'POST',
    data
  })
}

export function getMerchList(data) {
  return request({
    url: '/merch/getMerchReport',
    method: 'POST',
    data
  })
}

export function getUserInfo(data) {
  return request({
    url: '/pay/user/info',
    method: 'POST',
    data
  })
}

export function edit(data) {
  return request({
    url: '/pay/user/edit',
    method: 'POST',
    data
  })
}

export function del(data) {
  return request({
    url: '/pay/user/delete',
    method: 'POST',
    data
  })
}

export function updataUserState(data){
	return request({
	  url: '/pay/user/updataUserState',
	  method: 'POST',
	  data
	})
}

export function selectRole(data){
	return request({
	  url: '/pay/user/selectRole',
	  method: 'POST',
	  data
	})
}

export function resetPassword(data) {
  return request({
    url: '/pay/user/resetPassword',
    method: 'post',
    data
  })
}

