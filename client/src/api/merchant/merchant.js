import request from '@/utils/request'

export function getpageList(data) {
  return request({
    url: '/pay/merchant/list',
    method: 'POST',
    data
  })
}


export function getMerchantInfo(data) {
  return request({
    url: '/pay/merchant/info',
    method: 'POST',
    data
  })
}

export function updataMerchantStatus(data){
	return request({
	  url: '/pay/merchant/updataMerchantStatus',
	  method: 'POST',
	  data
	})
}


export function updataMerchantOpenChannel(data){
	return request({
	  url: '/pay/merchant/updataMerchantOpenChannel',
	  method: 'POST',
	  data
	})
}

export function branchlist(data) {
  return request({
    url: '/pay/merchant/branchlist',
    method: 'POST',
    data
  })
}

export function edit(data) {
  return request({
    url: '/pay/merchant/edit',
    method: 'POST',
    data
  })
}

export function del(data) {
  return request({
    url: '/pay/merchant/delete',
    method: 'POST',
    data
  })
}

export function uploadFile(data) {
  return request({
    url: '/pay/merchant/uploadFile',
    method: 'POST',
    data
  })
}
