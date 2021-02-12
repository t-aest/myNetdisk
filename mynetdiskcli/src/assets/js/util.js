export const FileTypes = {
  PDFTYPE: 'application/pdf',
  TEXTTYPE: 'text/plain',
  CSSTYPE: 'text/css',
  CSVTYPE: 'text/csv',
  DOCTYPE: 'application/msword',
  GIFTYPE: 'image/gif',
  HTMLTYPE: 'text/html',
  JPGTYPE: 'image/jpeg',
  JSONTYPE: 'application/json',
  MP3TYPE: 'audio/mpeg',
  MP4TYPE: 'audio/mp4' || 'video/mp4',
  PNGTYPE: 'image/png',
  XLSTYPE: 'application/vnd.ms-excel',
  XMLTYPE: 'application/xml' || 'text/xml',
  ZIPTYPE: 'application/zip',
  UNKNOWTYPE: 'application/octet-stream',
  FOLDER: 'folder',
  getIconByType (fileType) {
    if (fileType === this.PDFTYPE) {
      return 'static/img/pdf-icon.png'
    } else if (fileType === this.TEXTTYPE) {
      return 'static/img/txt-icon.png'
    } else if (fileType === this.CSSTYPE) {
      return 'static/img/css-icon.png'
    } else if (fileType === this.CSSTYPE) {
      return 'static/img/csv-icon.png'
    } else if (fileType === this.DOCTYPE) {
      return 'static/img/doc-icon.png'
    } else if (fileType === this.GIFTYPE) {
      return 'static/img/gif-icon.png'
    } else if (fileType === this.HTMLTYPE) {
      return 'static/img/html-icon.png'
    } else if (fileType === this.JPGTYPE) {
      return 'static/img/jpg-icon.png'
    } else if (fileType === this.JSONTYPE) {
      return 'static/img/json-icon.png'
    } else if (fileType === this.MP3TYPE) {
      return 'static/img/mp3-icon.png'
    } else if (fileType === this.MP4TYPE) {
      return 'static/img/mp4-icon.png'
    } else if (fileType === this.PNGTYPE) {
      return 'static/img/png-icon.png'
    } else if (fileType === this.XLSTYPE) {
      return 'static/img/txt-icon.png'
    } else if (fileType === this.XMLTYPE) {
      return 'static/img/xml-icon.png'
    } else if (fileType === this.ZIPTYPE) {
      return 'static/img/zip-icon.png'
    } else if (fileType === this.FOLDER) {
      return 'static/img/folder-icon.png'
    } else {
      return 'static/img/unknown-icon.png'
    }
  }
}
export const Util = {
  index: -1,
  formatdate: function (dateString, type) {
    let date = new Date(dateString)
    let year = date.getFullYear()
    let month = addzero(date.getMonth() + 1)
    let weekday = addzero(date.getDate())
    let hour = addzero(date.getHours())
    let minute = addzero(date.getMinutes())
    let second = addzero(date.getSeconds())

    function addzero (value) {
      if (value < 10) {
        value = '0' + value
      }
      return value
    }

    if (type === 1) {
      return (year + '-' + month + '-' + weekday)
    } else {
      return (year + '-' + month + '-' + weekday + ' ' + hour + ':' + minute + ':' + second)
    }
  },
  getIcon (fileType) {
    if (fileType === 'text/plain') {
      return 'static/img/image-icon.png'
    } else {
      return 'el-icon-view'
    }
  },
  /**
   * 空校验 null或""都返回true
   */
  isEmpty (obj) {
    if ((typeof obj === 'string')) {
      return !obj || obj.replace(/\s+/g, '') === ''
    } else {
      return (!obj || JSON.stringify(obj) === '{}' || obj.length === 0)
    }
  },
  /**
   * 移除对象数组中的对象
   * @param array
   * @param obj
   * @returns {number}
   */
  removeObj (array, obj) {
    let index = -1
    for (let i = 0; i < array.length; i++) {
      if (array[i] === obj) {
        array.splice(i, 1)
        index = i
        break
      }
    }
    return index
  },
  /**
   * 10进制转62进制
   * @param number
   * @returns {string}
   * @private
   */
  _10to62 (number) {
    let chars = '0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ'
    let radix = chars.length
    let arr = []
    do {
      let mod = number % radix
      number = (number - mod) / radix
      arr.unshift(chars[mod])
    } while (number)
    return arr.join('')
  },
  /**
   * 随机生成[len]长度的[radix]进制数
   * @param len
   * @param radix 默认62
   * @returns {string}
   */
  uuid (len, radix) {
    let chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('')
    let uuid = []
    radix = radix || chars.length

    for (let i = 0; i < len; i++) {
      uuid[i] = chars[0 | Math.random() * radix]
    }

    return uuid.join('')
  },
}
