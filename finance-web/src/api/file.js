import request from '@/utils/request'

export function uploadFile(file, module = 'contract') {
  const fd = new FormData()
  fd.append('file', file)
  fd.append('module', module)
  return request({ url: '/file/upload', method: 'post', data: fd })
}

export function importContract(file, contractType, contractName) {
  const fd = new FormData()
  fd.append('file', file)
  fd.append('contractType', contractType)
  if (contractName) fd.append('contractName', contractName)
  return request({ url: '/contract/import', method: 'post', data: fd })
}

function triggerBlobDownload(blob, fileName) {
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = fileName || 'document'
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
}

export async function downloadFile(path, fileName) {
  const blob = await request({
    url: '/file/download',
    method: 'get',
    params: { path },
    responseType: 'blob'
  })
  triggerBlobDownload(blob, fileName)
}

export async function previewFile(path) {
  const blob = await request({
    url: '/file/preview',
    method: 'get',
    params: { path },
    responseType: 'blob'
  })
  const url = URL.createObjectURL(blob)
  window.open(url, '_blank')
}
