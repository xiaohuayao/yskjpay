import request from '@/utils/request'

export function transactionList(data) {
  return request({
    url: '/user/list',
    method: 'POST',
    data
  })
}

export function topData(data) {
  return request({
    url: '/order/report',
    method: 'POST',
    data
  })
}

//首页汇总
export function collect(data) {
  return request({
    url: '/pay/order/homePageCollect',
    method: 'POST',
    data
  })
}

//获取最近七天日期
export function latelyDate(data) {
  return request({
    url: '/pay/order/latelyDate',
    method: 'POST',
    data
  })
}

//获取最近七天汇总
export function latelyLine(data) {
  return request({
    url: '/pay/order/latelyLine',
    method: 'POST',
    data
  })
}