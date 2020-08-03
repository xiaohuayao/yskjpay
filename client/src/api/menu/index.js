import request from '@/utils/request'

// 查询菜单列表
export function listMenu(data) {
  return request({
    url: '/pay/menu/menuTree',
    method: 'post',
    data
  })
}

// // 查询菜单详细
// export function getMenu(data) {
//   return request({
//     url: '/pay/menu/info',
//     method: 'post',
//     data
//   })
// }

// 查询菜单详细
export function getMenu(menuId) {
  return request({
    url: '/pay/menu/' + menuId,
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: '/pay/menu/save',
    method: 'post',
    data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: '/pay/menu/update',
    method: 'post',
    data
  })
}

// // 删除菜单
// export function delMenu(data) {
//   return request({
//     url: '/pay/menu/delete',
//     method: 'post',
//     data
//   })
// }

// 删除菜单
export function delMenu(menuId) {
  return request({
    url: '/pay/menu/' + menuId,
    method: 'delete'
  })
}