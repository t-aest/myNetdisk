<template>
  <div>
    <el-container style="height: 100%" direction="vertical">
      <el-header>
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item disabled style="opacity:1" index="1">myNetdisk</el-menu-item>
          <el-menu-item index="2">网盘</el-menu-item>
          <el-menu-item index="3">通讯录</el-menu-item>
          <el-menu-item index="4">历史记录</el-menu-item>
        </el-menu>
      </el-header>
      <el-container>
        <el-aside width="200px" style="border: solid 1px #e6e6e6;text-align: center">
          <el-menu :default-openeds="['1', '3']" mode="vertical">
            <!--              <template slot="title">全部文件</template>-->
            <el-menu-item index="1-1">
              <template slot="title"><i class="el-icon-document-copy"></i>全部文件</template>
            </el-menu-item>
            <el-menu-item index="1-2">图片</el-menu-item>
            <el-menu-item index="1-3">文档</el-menu-item>
            <el-menu-item index="1-4">视频</el-menu-item>
            <el-menu-item index="1-5">音乐</el-menu-item>
            <el-menu-item index="1-6">其他</el-menu-item>
            <el-menu-item index="2">
              <template slot="title"><i class="el-icon-link"></i>我的分享</template>
            </el-menu-item>
            <el-menu-item index="3">
              <template slot="title"><i class="el-icon-delete"></i>回收站</template>
            </el-menu-item>
<!--            <el-menu-item index="4">-->
<!--              <template slot="title"><i class="el-icon-top"></i>上传列表</template>-->
<!--            </el-menu-item>-->
<!--            <el-menu-item index="5">-->
<!--              <template slot="title"><i class="el-icon-bottom"></i>下载列表</template>-->
<!--            </el-menu-item>-->
            <el-footer>
              <!--              <el-progress :percentage="50" "></el-progress>-->
              <el-progress type="circle" :percentage="100" stroke-width="25" color="#ebeef5"></el-progress>
              <br>
              <label>内存占用：</label><span>4156G/10T</span><br>
              <label>当前时间：</label><br><span>{{dateString(nowTime)}}</span>

            </el-footer>
          </el-menu>
        </el-aside>
        <el-container>
          <el-main>
            <el-button @click="upload">上传</el-button>
            <el-button @click="uploadFolder">上传文件夹</el-button>
            <el-button-group>
              <!--              <el-button @click="createFolder" plain>-->
              <!--                <i class="el-icon-folder-add el-icon&#45;&#45;left">&nbsp;新建文件夹</i>-->
              <!--              </el-button>-->
              <CreateFolder ref="createFolder" style="float: left" v-bind:paren-id="currentId"
                            v-bind:after-create="afterCreate">新建文件夹
              </CreateFolder>
              <el-button plain>
                <i class="el-icon-tickets el-icon--left">&nbsp;新建文件</i>
              </el-button>
              <el-button @click="refresh" plain>
                <i class="el-icon-refresh el-icon--left">刷新</i>
              </el-button>
              <el-button plain>
                <i class="el-icon-link el-icon--left">&nbsp;分享</i>
              </el-button>
              <el-button plain>
                <i class="el-icon-download el-icon--left">&nbsp;下载</i>
              </el-button>
              <el-button @click="delFile(0)" plain>
                <i class="el-icon-delete el-icon--left">&nbsp;删除</i>
              </el-button>
<!--              <el-button @click="rename" plain>&nbsp;重命名</el-button>-->

              <ReName class="renameClick" ref="reName" style="float: left"
                      v-bind:is-show="showRenameBtn"
                      v-bind:file-id="selectFileId"
                      v-bind:filename="fileName"
                      v-bind:file-suffix="fileSuffix"
                      v-bind:after-rename="afterRename">重命名
              </ReName>
              <MoveAndCopy class="moveClick" ref="moveFile" v-bind:is-show="showRenameBtn" v-bind:move-or-copy="1" v-bind:file-id="selectFileId" v-bind:after-move-or-copy="afterMoveOrCopy" style="float: left"></MoveAndCopy>
              <MoveAndCopy class="moveClick" ref="moveFile" v-bind:is-show="showRenameBtn" v-bind:move-or-copy="0" v-bind:file-id="selectFileId" v-bind:after-move-or-copy="afterMoveOrCopy" style="float: left"></MoveAndCopy>
            </el-button-group>

            <el-breadcrumb separator-class="el-icon-arrow-right">
              <!--              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>-->
              <el-breadcrumb-item @click.native="testBread(item)"
                                  v-for="item in Breadcrumb"
                                  :key="item.id"
                                  :to="item.path"
              >
                {{ item.name }}
              </el-breadcrumb-item>
            </el-breadcrumb>
            <el-table
              ref="multipleTable"
              v-loading="loading"
              :data="tableData"
              tooltip-effect="dark"
              style="width: 100%"
              @cell-mouse-enter="onMouseEnter"
              @cell-mouse-leave="onMouseLeave"
              @selection-change="handleSelectionChange">
              <el-table-column
                type="selection"
                width="100%">
              </el-table-column>
              <el-table-column
                label="文件名"
                prop="name"
                min-width="200">
                <template slot-scope="scope">
                  <div @click="fileClick(scope)">
                    <img :src="getIcon(scope)">&nbsp;
<!--                    <el-input class="renamefile" v-model="fileName">-->
<!--                      <template slot="append">.{{fileSuffix}}</template>-->
<!--                    </el-input>-->
                    <span class="filename">{{ scope.row.name }}</span>
                  </div>
                    <div style="float: right; position: relative;left: 3px;bottom: 36px;height: 3px" class="more-oper" v-show="showMoreOper">
                      <el-dropdown style="float: right;top: 9px">
                    <span class="el-dropdown-link"><i class="el-icon-more el-icon--right"></i>
                   </span>
                        <el-dropdown-menu slot="dropdown">
                          <el-dropdown-item>移动到</el-dropdown-item>
                          <el-dropdown-item>复制到</el-dropdown-item>
                          <el-dropdown-item @click.native="rename(scope)">重命名</el-dropdown-item>
                          <el-dropdown-item @click.native="delFile(scope)">删除</el-dropdown-item>
                        </el-dropdown-menu>
                      </el-dropdown>
                      &nbsp;&nbsp;&nbsp;
                      <el-button @click="download(scope.row.id)" type="text" style="float: right"
                                 icon="el-icon-download"></el-button>
                      &nbsp;
                      <el-button type="text" style="float: right" icon="el-icon-share"></el-button>
                    </div>

                </template>
              </el-table-column>
              <el-table-column
                prop="size"
                label="大小"
                header-align="center"
                align="center"
                min-width="60">
                <template>--</template>
              </el-table-column>
              <el-table-column
                label="修改日期"
                header-align="center"
                align="center"
                min-width="100"
                show-overflow-tooltip>
                <template slot-scope="scope">{{ dateString(scope.row.createdAt) }}</template>
              </el-table-column>
            </el-table>
          </el-main>
          <el-footer>版权所有 &copy; xxxxxxxx &nbsp;&nbsp; 24小时客户服务热线：400-8879-597</el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>
<script>
import Bus from '@/assets/js/bus'
import {apiConfig} from '../request/api'
import {FileTypes, Util} from '../assets/js/util'
import CreateFolder from './CreateFolder'
import ReName from './ReName'
import MoveAndCopy from './MoveAndCopy'
import {v4 as uuidv4} from 'uuid'
import $ from 'jquery'

export default {
  name: 'HelloWorld',
  components: {CreateFolder, ReName, MoveAndCopy},
  data () {
    return {
      msg: 'display content',
      tabPosition: 'left',
      tableData: [],
      showMoreOper: false,
      showRenameBtn: false,
      nowTime: new Date(),
      loading: false,
      multipleSelection: [],
      currentId: 0,
      fileName: 'tes',
      fileSuffix: 'pdf',
      parentPath: '',
      selectFileId: '',
      Breadcrumb: [
        {path: '/main', parentId: 0, name: '首页'}
      ]
    }
  },
  mounted () {
    let parentId = this.currentId
    this.list(parentId)
    // 文件选择后的回调
    Bus.$on('fileAdded', () => {
      console.log('文件已选择')
    })
    // 文件上传成功的回调
    Bus.$on('fileSuccess', (res) => {
      let self = this
      console.log('文件已sucess   ')
      console.log(self.tableData.indexOf(res.data) === -1)
      console.log(res.data)
      let shardIndex = res.data.shardIndex
      let shardTotal = res.data.shardTotal
      if (res.data.fileType !== 'folder') {
        if (shardIndex === shardTotal) {
          self.tableData.push(res.data)
        }
      } else {
        if (Util.index++ === -1) {
          self.tableData.push(res.data)
        }
      }
    })
  },
  computed: {},
  methods: {
    dateString (date) {
      return Util.formatdate(date, 0)
    },
    rename (scope) {
      let self = this
      console.log('click rename')
      // console.log(scope)
      self.selectFileId = scope.row.id
      let name = scope.row.name
      let index = name.lastIndexOf('.')
      if (index === -1) {
        self.fileName = name
        self.fileSuffix = ''
      } else {
        self.fileName = name.slice(0, index)
        self.fileSuffix = name.slice(index + 1, name.length)
      }
      $('#rename-click').click()
    },
    testBread (item) {
      console.log(item)
      let self = this
      let index = self.Breadcrumb.indexOf(item)
      console.log(self.Breadcrumb.indexOf(item))
      console.log(self.Breadcrumb.slice(0, index + 1))
      self.Breadcrumb = self.Breadcrumb.slice(0, index + 1)
      self.list(item.parentId)
      self.currentId = item.parentId
    },
    fileClick (scope) {
      let self = this
      if (scope.row.fileType === 'folder') {
        // eslint-disable-next-line standard/object-curly-even-spacing
        let bread = {path: '/', parentId: self.currentId, name: scope.row.name}
        self.currentId = scope.row.id
        self.parentPath = scope.row.name
        self.list(self.currentId)
        self.Breadcrumb.push(bread)
      } else {
        console.log(scope)
      }
    },
    handleSelectionChange (val) {
      let self = this
      self.multipleSelection = val
      console.log(self.tableData)
      console.log(self.multipleSelection)
      console.log(val)
      if (self.multipleSelection.length === 1) {
        self.showRenameBtn = true
        self.selectFileId = self.multipleSelection[0].id
        let name = self.multipleSelection[0].name
        let index = name.lastIndexOf('.')
        self.fileName = name.slice(0, index)
        self.fileSuffix = name.slice(index + 1, name.length)
        if (self.filename === self.fileSuffix) {
          self.fileSuffix = ''
        }
      } else {
        self.showRenameBtn = false
      }
    },
    onMouseEnter (row, column, cell, event) {
      // eslint-disable-next-line no-unused-vars
      let self = this
      this.showMoreOper = true
    },
    onMouseLeave (row, column, cell, event) {
      // eslint-disable-next-line no-unused-vars
      let self = this
      this.showMoreOper = false
    },
    // 控制icon显示
    getIcon (scope) {
      return FileTypes.getIconByType(scope.row.fileType)
    },
    // 刷新文件列表
    refresh () {
      this.list(this.currentId)
    },
    list (data) {
      let self = this
      self.loading = true
      apiConfig.listFiles(data)
        .then(res => {
          console.log(res.data)
          self.tableData = res.data
          self.loading = false
        }).catch(err => {
          console.log(err)
          self.loading = false
        })
    },
    delFile (scope) {
      let self = this
      let val = self.multipleSelection
      if (scope === 0) {
        if (val) {
          // 将选中数据遍历
          val.forEach((va, index) => {
            // 遍历源数据
            self.tableData.forEach((v, i) => {
              // 如果选中数据和源数据的某一条唯一标识符相等，删除对应的源数据
              if (va.id === v.id) {
                console.log(va)
                self.tableData.splice(i, 1)
                apiConfig.delFile(va.id)
                  .then(res => {
                    console.log(res)
                    if (res.code === 0) {
                      self.$message.success('file: ' + res.data.name + ' 删除成功！')
                    } else {
                      self.$message.error('file: ' + res.data.name + ' 删除失败！')
                    }
                  }).catch(err => {
                    self.$message.error(err)
                  })
              }
            })
          })
          self.$refs.multipleTable.clearSelection()
        }
      } else {
        apiConfig.delFile(scope.row.id)
          .then(res => {
            console.log(res)
            if (res.code === 0) {
              self.tableData.splice(scope.$index, 1)
              self.$message.success('删除成功！')
            } else {
              self.$message.error('删除失败！')
            }
          }).catch(err => {
            self.$message.error(err)
          })
      }
    },
    download (id) {
      let url = '/api/file/download/' + id
      window.open(url)
      // apiConfig.download(id)
      //   .then(res => {
      //   }).catch(err => {
      //     console.log(err)
      //     self.loading = false
      //   })
    },
    afterCreate (resp) {
      let self = this
      self.tableData.push(resp)
    },
    afterRename (resp) {
      let self = this
      console.log(resp)
      self.refresh()
    },
    afterMoveOrCopy (resp) {
      let self = this
      console.log(resp)
      self.refresh()
    },
    upload () {
      // 打开文件选择框
      Bus.$emit('openUploader', {
        id: '1111',
        fileType: '',
        parentPath: this.parentPath,
        parentId: this.currentId// 传入的参数
      })
    },
    uploadFolder () {
      console.log('uploadFolder')
      Util.index = -1
      // 打开文件选择框
      Bus.$emit('openUploaderFolder', {
        id: '1111',
        fileType: 'folder',
        parentPath: this.parentPath,
        folderId: uuidv4().replaceAll('-', ''),
        parentId: this.currentId// 传入的参数
      })
    },
    destroyed () {
      Bus.$off('fileAdded')
      Bus.$off('fileSuccess')
    }
  }
}
</script>
<style scoped lang="scss">
.el-dropdown {
  vertical-align: top;
}

.el-table {
  border-top: solid 1px #e6e6e6;
}

.el-breadcrumb {
  padding-top: 7px;
  padding-bottom: 7px;
}

.el-dropdown + .el-dropdown {
  margin-left: 15px;
}

.el-icon-arrow-down {
  font-size: 12px;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

/*.el-button{*/
/*  background-color: #ffffff;*/
/*  border-color: skyblue;*/
/*  color: skyblue;*/
/*  padding: 10px 10px 10px 10px;*/
/*  border-radius: 5px;*/
/*}*/
.el-menu {
  border-right-style: none;
  /*align-items: center; !* 水平居中 *!*/
  /*justify-content: center; !* 垂直居中 *!*/
}

.el-header, .el-footer {
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  overflow: auto;
  color: #333;
  float: left;
  height: calc(100vh - 60px);
}

.el-main {
  padding: 10px;
  color: #333;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-icon-arrow-down {
  font-size: 12px;
}
</style>
