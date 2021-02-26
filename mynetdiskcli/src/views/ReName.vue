<template>
  <div>
    <el-button v-show="isShow" plain id="rename-click">
<!--      <i class="el-icon-folder-add el-icon&#45;&#45;left">&nbsp;新建文件夹</i>-->
      重命名
    </el-button>
    <el-dialog
      title="文件重命名"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
<!--      <el-input placeholder="请输入文件夹名称" v-model="input2">-->
<!--        <template slot="append">.com</template>-->
<!--      </el-input>-->
      <el-input
        v-model="filename"
        clearable>
        <template slot="append">.{{fileSuffix}}</template>
      </el-input>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="rename">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

// import Bus from '../assets/js/bus'
import {apiConfig} from '../request/api'
export default {
  name: 'ReName',
  props: {
    isShow: {
      type: Boolean,
      default: false
    },
    fileId: {
      default: 0
    },
    filename: {
      default: ''
    },
    fileSuffix: {
      default: ''
    },
    afterRename: {
      type: Function,
      default: null
    }
  },
  data () {
    return {
      dialogVisible: false,
      input: ''
    }
  },
  mounted () {
    // Bus.$on('CreateFolder', query => {
    //   this.params = query || {}
    //   console.log('mounted')
    //   this.dialogVisible = true
    // })
  },
  methods: {
    handleClose (done) {
      let self = this
      self.dialogVisible = false
    },
    renameClick () {
      console.log('click rename')
      this.dialogVisible = true
    },
    rename () {
      let self = this
      let filename = self.filename
      console.log('save rename')
      self.dialogVisible = false
      // var self = this
      console.log(self.filename)
      console.log(self.fileSuffix)
      if (self.fileSuffix !== '') {
        filename = self.filename + '.' + self.fileSuffix
      }
      let params = {
        'fileId': self.fileId,
        'filename': filename
      }
      apiConfig.rename(params)
        .then(res => {
          if (res.code === 0) {
            self.$message.success('重命名成功')
            self.afterRename(res.data)
            self.dialogVisible = false
          } else {
            self.$message.error('重命名失败')
            self.dialogVisible = false
          }
        }).catch(err => {
          self.$message.error('重命名失败 ：' + err)
          self.dialogVisible = false
        })
    }
  }
  // destroyed () {
  //   Bus.$off('CreateFolder')
  // }
}
</script>

<style scoped>

</style>
