import request from '@/utils/request'

export function getpageList(data) {
  return request({
    url: '/pay/club/list',
    method: 'POST',
    data
  })
}

export function edit(data) {
  return request({
    url: '/pay/club/edit',
    method: 'POST',
    data
  })
}

export function del(data) {
  return request({
    url: '/pay/club/delete',
    method: 'POST',
    data
  })
}

export function info(data) {
  return request({
    url: '/pay/club/info',
    method: 'POST',
    data
  })
}

export function getMenu(data) {
  return request({
    url: '/pay/menu/findMenu',
    method: 'POST',
    data
  })
}