import request from '@/utils/request'

export function getpageListMerch(data) {
  return request({
    url: '/pay/order/collectListMerch',
    method: 'POST',
    data
  })
}

export function getpageListMerchPlus(data) {
  return request({
    url: '/pay/order/collectListMerchPlus',
    method: 'POST',
    data
  })
}

export function getTimes(data) {
  return request({
    url: '/pay/order/queryTime',
    method: 'POST', 
    data
  })
}


export function getpageListBranch(data) {
  return request({
    url: '/pay/order/collectListBranch',
    method: 'POST',
    data
  })
}

export function getpageListClub(data) {
  return request({
    url: '/pay/order/collectListClub',
    method: 'POST',
    data
  })
}
 