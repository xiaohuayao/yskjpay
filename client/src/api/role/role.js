import request from '@/utils/request'

export function getpageList(data) {
  return request({
    url: '/pay/role/list',
    method: 'post',
    data
  })
}

export function del(data) {
  return request({
    url: '/pay/role/delete',
    method: 'POST',
    data
  })
}

//修改权限
export function setTree(data) {
  return request({
    url: '/pay/role/setTree',
    method: 'POST',
    data
  })
}

//获取权限详情
export function getTree(data) {
  return request({
    url: '/pay/role/getTree',
    method: 'POST',
    data
  })
}

//新增权限
export function saveRole(data) {
  return request({
    url: '/pay/role/saveRole',
    method: 'POST',
    data
  })
}

// 获取菜单
export function listMenu(data) {
  return request({
    url: '/pay/menu/menuTree',
    method: 'post',
    data
  })
}
