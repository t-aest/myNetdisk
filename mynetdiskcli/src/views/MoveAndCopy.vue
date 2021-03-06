<template>
  <div>
    <el-button v-show="isShow" @click="openCreateFolder" plain >
      {{dialogTitle}}
    </el-button>
    <el-dialog
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <span slot="title" class="dialog-title">{{dialogTitle}}</span>
<!--      <el-input placeholder="请输入文件夹名称" v-model="input2">-->
<!--        <template slot="append">.com</template>-->
<!--      </el-input>-->
      <el-tree
        class="filter-tree"
        :data="folderData"
        :props="defaultProps"
        default-expand-all
        :filter-node-method="filterNode"
        @node-click="handleNodeClick"
        ref="tree">
         <span class="custom-tree-node" slot-scope="{ data }">
            <span>
                <i :class="data.icon"></i>&nbsp;{{ data.name }}<br>
            </span>
        </span>
      </el-tree>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="submit">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

// import Bus from '../assets/js/bus'
import {apiConfig} from '../request/api'
export default {
  name: 'MoveFile',
  props: {
    isShow: {
      type: Boolean,
      default: false
    },
    moveOrCopy: {
      default: 0
    },
    fileId: {
      default: 0
    },
    parenId: {
      default: 0
    },
    afterMoveOrCopy: {
      type: Function,
      default: null
    }
  },
  data () {
    return {
      dialogVisible: false,
      input: '',
      filterText: '',
      dialogTitle: '',
      folderData: [],
      selectFileId: '0',
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  mounted () {
    let self = this
    if (self.moveOrCopy === 0) {
      self.dialogTitle = '移动到'
    }
    if (self.moveOrCopy === 1) {
      self.dialogTitle = '复制到'
    }
    self.listFolder()
  },
  watch: {
    filterText (val) {
      this.$refs.tree.filter(val)
    }
  },
  methods: {
    handleClose (done) {
      let self = this
      self.dialogVisible = false
    },
    handleNodeClick (data) {
      console.log(data)
      let self = this
      self.selectFileId = data.id
    },
    openCreateFolder () {
      console.log('asd')
      this.listFolder()
      this.dialogVisible = true
    },
    submit () {
      var self = this
      let params = {
        'fileId': self.fileId,
        'targetFileId': self.selectFileId,
        'operFlag': self.moveOrCopy
      }
      apiConfig.moveorcopy(params)
        .then(res => {
          if (res.code === 0) {
            self.$message.success('操作成功')
            self.afterMoveOrCopy(res.data)
            self.dialogVisible = false
          } else {
            self.$message.error('操作失败')
            self.dialogVisible = false
          }
        }).catch(err => {
          self.$message.error('操作失败 ：' + err)
          self.dialogVisible = false
        })
    },
    listFolder () {
      let self = this
      apiConfig.listFolders(null)
        .then(res => {
          if (res.code === 0) {
            self.folderData = res.data
          } else {
          }
        }).catch(err => {
          console.log(err)
        })
    },
    filterNode (value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    }
  }
  // destroyed () {
  //   Bus.$off('CreateFolder')
  // }
}
</script>

<style scoped>
  .dialog-title{
    font-size: 15px;
  }
</style>
