/** 解析合同 attachment 字段（JSON 字符串或对象） */
export function parseAttachment(raw) {
  if (!raw) return null
  if (typeof raw === 'object') return raw
  try {
    return JSON.parse(raw)
  } catch {
    return null
  }
}

export function attachmentFileName(raw) {
  const info = parseAttachment(raw)
  return info?.fileName || ''
}

export function hasAttachment(raw) {
  return !!parseAttachment(raw)?.path
}
