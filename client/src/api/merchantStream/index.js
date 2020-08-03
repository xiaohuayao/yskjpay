import request from '@/utils/request'

export function getpageList(data) {
  return request({
    url: '/pay/order/streamList',
    method: 'POST',
    data
  })
}

// export function getTimes(data) {
//   return request({
//     url: '/pay/order/queryTime',
//     method: 'POST',
//     data
//   })
// }

export function edit(data) {
  return request({
    url: '/pay/refund/refundAmount',
    method: 'POST',
    data
  })
}

export function exportExcelOrder(data) {
  return request({
    url: '/pay/order/exportExcelOrder',
    method: 'get',
     params: data
  })
}
