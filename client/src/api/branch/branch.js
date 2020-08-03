import request from '@/utils/request'

export function getpageList(data) {
  return request({
    url: '/pay/branch/list',
    method: 'POST',
    data
  })
}

export function edit(data) {
  return request({
    url: '/pay/branch/edit',
    method: 'POST',
    data
  })
}

export function del(data) {
  return request({
    url: '/pay/branch/delete',
    method: 'POST',
    data
  })
}

export function info(data) {
  return request({
    url: '/pay/branch/info',
    method: 'POST',
    data
  })
}