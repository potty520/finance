/** 凭证打印辅助 */

const STORAGE_KEY = 'voucher-print-paper'

export const PAPER_PRESETS = [
  { key: 'a4-landscape', label: 'A4 横向', width: 297, height: 210 },
  { key: 'a4-portrait', label: 'A4 纵向', width: 210, height: 297 },
  { key: 'a5-landscape', label: 'A5 横向', width: 210, height: 148 },
  { key: 'a5-portrait', label: 'A5 纵向', width: 148, height: 210 },
  { key: 'voucher-240x140', label: '凭证纸 240×140mm', width: 240, height: 140 },
  { key: 'voucher-210x120', label: '凭证纸 210×120mm', width: 210, height: 120 },
  { key: 'letter-landscape', label: 'Letter 横向', width: 279, height: 216 },
  { key: 'custom', label: '自定义尺寸', width: 240, height: 140 }
]

export const DEFAULT_PAPER_OPTIONS = {
  preset: 'a4-landscape',
  widthMm: 297,
  heightMm: 210,
  marginTop: 12,
  marginRight: 12,
  marginBottom: 12,
  marginLeft: 12,
  scale: 100
}

export function loadPaperSettings() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (!raw) return { ...DEFAULT_PAPER_OPTIONS }
    return { ...DEFAULT_PAPER_OPTIONS, ...JSON.parse(raw) }
  } catch (e) {
    return { ...DEFAULT_PAPER_OPTIONS }
  }
}

export function savePaperSettings(options) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(options))
}

function clampNum(val, min, max) {
  const n = Number(val)
  if (Number.isNaN(n)) return min
  return Math.min(max, Math.max(min, n))
}

export function resolvePaperSize(options = {}) {
  const merged = { ...DEFAULT_PAPER_OPTIONS, ...options }
  if (merged.preset === 'custom') {
    return {
      width: clampNum(merged.widthMm, 50, 600),
      height: clampNum(merged.heightMm, 50, 600)
    }
  }
  const preset = PAPER_PRESETS.find(p => p.key === merged.preset)
  if (preset) return { width: preset.width, height: preset.height }
  return { width: 297, height: 210 }
}

export function getPresetLabel(presetKey) {
  return PAPER_PRESETS.find(p => p.key === presetKey)?.label || '自定义'
}

export function paperSizeText(options = {}) {
  const { width, height } = resolvePaperSize(options)
  const label = options.preset === 'custom' ? '自定义' : getPresetLabel(options.preset)
  return `${label}（${width} × ${height} mm）`
}

function contentScale(options = {}) {
  const userScale = clampNum(options.scale, 50, 150) / 100
  const { width, height } = resolvePaperSize(options)
  const area = width * height
  let autoScale = 1
  if (area <= 33600) autoScale = 0.82
  else if (area <= 45000) autoScale = 0.9
  else if (area <= 62400) autoScale = 0.95
  return Math.min(1.2, userScale * autoScale)
}

export function buildPrintStyles(options = {}) {
  const merged = { ...DEFAULT_PAPER_OPTIONS, ...options }
  const { width, height } = resolvePaperSize(merged)
  const mt = clampNum(merged.marginTop, 0, 50)
  const mr = clampNum(merged.marginRight, 0, 50)
  const mb = clampNum(merged.marginBottom, 0, 50)
  const ml = clampNum(merged.marginLeft, 0, 50)
  const scale = contentScale(merged)

  return `
  @page { size: ${width}mm ${height}mm; margin: ${mt}mm ${mr}mm ${mb}mm ${ml}mm; }
  * { box-sizing: border-box; }
  body { margin: 0; font-family: "SimSun", "Songti SC", "Microsoft YaHei", serif; color: #000; }
  .voucher-sheet {
    width: ${scale < 1 ? (100 / scale) : 100}%;
    padding: 4px 8px;
    transform: scale(${scale});
    transform-origin: top left;
  }
  .voucher-title { text-align: center; font-size: ${Math.round(22 * scale)}px; font-weight: bold; letter-spacing: 8px; margin: 0 0 8px; }
  .voucher-company { text-align: center; font-size: ${Math.round(14 * scale)}px; margin-bottom: 10px; }
  .voucher-meta { display: flex; justify-content: space-between; font-size: ${Math.round(13 * scale)}px; margin-bottom: 8px; flex-wrap: wrap; gap: 4px 12px; }
  .voucher-meta span { margin-right: 16px; }
  table { width: 100%; border-collapse: collapse; table-layout: fixed; font-size: ${Math.round(12 * scale)}px; }
  th, td { border: 1px solid #000; padding: ${Math.max(3, Math.round(6 * scale))}px 4px; word-break: break-all; }
  th { text-align: center; font-weight: bold; background: #fafafa; }
  td.num { text-align: right; font-family: "Courier New", monospace; }
  td.center { text-align: center; }
  .total-row td { font-weight: bold; }
  .total-cn { margin-top: 8px; font-size: ${Math.round(13 * scale)}px; }
  .voucher-footer { display: flex; justify-content: space-between; margin-top: 16px; font-size: ${Math.round(13 * scale)}px; flex-wrap: wrap; gap: 8px; }
  .voucher-footer span { min-width: 18%; }
`
}

export function formatMoney(value) {
  const n = Number(value || 0)
  return n.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

export function parsePeriod(voucher) {
  if (voucher?.fiscalYear && voucher?.fiscalPeriod) {
    return { fiscalYear: voucher.fiscalYear, fiscalPeriod: voucher.fiscalPeriod }
  }
  const code = voucher?.periodCode || ''
  if (code.length >= 6) {
    return {
      fiscalYear: code.slice(0, 4),
      fiscalPeriod: Number(code.slice(4, 6))
    }
  }
  return { fiscalYear: '-', fiscalPeriod: '-' }
}

export function statusLabel(status) {
  const map = {
    DRAFT: '草稿',
    D: '草稿',
    APPROVING: '待审核',
    APPROVED: '已审核',
    A: '已审核',
    POSTED: '已过账',
    P: '已过账',
    REJECTED: '已驳回',
    R: '已驳回'
  }
  return map[status] || status || '-'
}

export function amountToChinese(amount) {
  const n = Math.round(Number(amount || 0) * 100)
  if (!n) return '零元整'
  const digits = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖']
  const units = ['', '拾', '佰', '仟']
  const bigUnits = ['', '万', '亿']

  const part = (num, suffix) => {
    if (!num) return ''
    let str = ''
    let zero = false
    for (let i = 0; i < 4; i++) {
      const d = Math.floor(num / Math.pow(10, 3 - i)) % 10
      if (d === 0) {
        zero = str.length > 0
      } else {
        if (zero) str += '零'
        str += digits[d] + units[3 - i]
        zero = false
      }
    }
    return str + suffix
  }

  const yuan = Math.floor(n / 100)
  const jiao = Math.floor((n % 100) / 10)
  const fen = n % 10
  let result = ''
  let temp = yuan
  let idx = 0
  while (temp > 0) {
    const seg = temp % 10000
    const segStr = part(seg, bigUnits[idx])
    if (segStr) result = segStr + result
    temp = Math.floor(temp / 10000)
    idx++
  }
  result = (result || '零') + '元'
  if (jiao) result += digits[jiao] + '角'
  if (fen) result += digits[fen] + '分'
  else if (!jiao) result += '整'
  return result
}

export function padEntries(entries, minRows = 6) {
  const list = (entries || []).map((e, i) => ({
    entryNo: e.entryNo || i + 1,
    summary: e.summary || '',
    subjectCode: e.subjectCode || '',
    subjectName: e.subjectName || '',
    debitAmount: Number(e.debitAmount || 0),
    creditAmount: Number(e.creditAmount || 0)
  }))
  while (list.length < minRows) {
    list.push({
      entryNo: list.length + 1,
      summary: '',
      subjectCode: '',
      subjectName: '',
      debitAmount: 0,
      creditAmount: 0
    })
  }
  return list
}

export function printVoucherHtml(html, title = '记账凭证', paperOptions = {}) {
  const styles = buildPrintStyles(paperOptions)
  const { width, height } = resolvePaperSize(paperOptions)
  const win = window.open('', '_blank', `width=${Math.min(1200, width * 4)},height=${Math.min(900, height * 4)}`)
  if (!win) {
    throw new Error('无法打开打印窗口，请检查浏览器是否拦截弹窗')
  }
  win.document.write(`<!DOCTYPE html><html><head><meta charset="utf-8"><title>${title}</title><style>${styles}</style></head><body>${html}</body></html>`)
  win.document.close()
  win.focus()
  setTimeout(() => {
    win.print()
    win.close()
  }, 300)
}

export function buildVoucherHtml(voucher, companyName = '财务管理系统') {
  const { fiscalYear, fiscalPeriod } = parsePeriod(voucher)
  const entries = padEntries(voucher.entries)
  const totalDebit = Number(voucher.totalDebit || 0)
  const totalCredit = Number(voucher.totalCredit || 0)
  const rows = entries.map(e => `
    <tr>
      <td class="center">${e.entryNo}</td>
      <td>${e.summary || '&nbsp;'}</td>
      <td class="center">${e.subjectCode || '&nbsp;'}</td>
      <td>${e.subjectName || '&nbsp;'}</td>
      <td class="num">${e.debitAmount ? formatMoney(e.debitAmount) : '&nbsp;'}</td>
      <td class="num">${e.creditAmount ? formatMoney(e.creditAmount) : '&nbsp;'}</td>
    </tr>
  `).join('')

  return `
    <div class="voucher-sheet">
      <div class="voucher-company">${companyName}</div>
      <h1 class="voucher-title">记 账 凭 证</h1>
      <div class="voucher-meta">
        <div>
          <span>凭证号：${voucher.voucherNo || '-'}</span>
          <span>日期：${voucher.voucherDate || '-'}</span>
          <span>期间：${fiscalYear}年 第${fiscalPeriod}期</span>
        </div>
        <div>
          <span>附单据：${voucher.attachCount ?? 0} 张</span>
          <span>状态：${statusLabel(voucher.status)}</span>
        </div>
      </div>
      <table>
        <thead>
          <tr>
            <th style="width:5%">序号</th>
            <th style="width:22%">摘要</th>
            <th style="width:12%">科目编码</th>
            <th style="width:22%">科目名称</th>
            <th style="width:19%">借方金额</th>
            <th style="width:20%">贷方金额</th>
          </tr>
        </thead>
        <tbody>
          ${rows}
          <tr class="total-row">
            <td colspan="4" class="center">合　计</td>
            <td class="num">${formatMoney(totalDebit)}</td>
            <td class="num">${formatMoney(totalCredit)}</td>
          </tr>
        </tbody>
      </table>
      <div class="total-cn">金额大写：${amountToChinese(totalDebit)}</div>
      <div class="voucher-footer">
        <span>制单：${voucher.createByName || voucher.creatorName || '-'}</span>
        <span>审核：${voucher.auditByName || voucher.auditorName || '-'}</span>
        <span>记账：${voucher.postBy ? (voucher.posterName || '已记账') : '-'}</span>
        <span>出纳：-</span>
      </div>
    </div>
  `
}
