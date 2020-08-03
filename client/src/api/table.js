import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/api/User/getPageList',
    method: 'get',
    params
  })
}
 