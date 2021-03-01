// 引入http.js
// eslint-disable-next-line no-unused-vars
import {get, post, put, $delete, noParameGet} from '../request/request'
// GET /zoos：列出所有动物园
// POST /zoos：新建一个动物园
// GET /zoos/ID：获取某个指定动物园的信息
// PUT /zoos/ID：更新某个指定动物园的信息（提供该动物园的全部信息）
// PATCH /zoos/ID：更新某个指定动物园的信息（提供该动物园的部分信息）
// DELETE /zoos/ID：删除某个动物园
// GET /zoos/ID/animals：列出某个指定动物园的所有动物
// DELETE /zoos/ID/animals/ID：删除某个指定动物园的指定动物
export const apiConfig = {
  // 文件列表查询接口
  listFiles: data => get('file/files', data),
  listFolders: data => noParameGet('file/folders'),
  delFile: id => $delete('file/delFile', id),
  mkdir: params => post('file/mkdir', params),
  rename: params => post('file/rename', params),
  download: id => get('file/download', id),
  login: params => post('user/login', params, 'json')
}
