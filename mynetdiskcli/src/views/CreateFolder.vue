<template>
  <div>
    <el-button @click="openCreateFolder" plain >
      <i class="el-icon-folder-add el-icon--left">&nbsp;新建文件夹</i>
    </el-button>
    <el-dialog
      title="创建文件夹"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
<!--      <el-input placeholder="请输入文件夹名称" v-model="input2">-->
<!--        <template slot="append">.com</template>-->
<!--      </el-input>-->
      <el-input
        placeholder="请输入文件夹名称"
        v-model="input"
        clearable>
      </el-input>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="create">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

// import Bus from '../assets/js/bus'
import {apiConfig} from '../request/api'
export default {
  name: 'CreateFolder',
  props: {
    isShow: {
      type: Boolean,
      default: false
    },
    parenId: {
      default: 0
    },
    afterCreate: {
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
    openCreateFolder () {
      console.log('asd')
      this.dialogVisible = true
    },
    create () {
      var self = this
      let params = {
        'parentId': self.parenId,
        'filename': self.input
      }
      apiConfig.mkdir(params)
        .then(res => {
          if (res.code === 0) {
            self.$message.success('创建成功')
            self.afterCreate(res.data)
            self.dialogVisible = false
          } else {
            self.$message.error('创建失败')
            self.dialogVisible = false
          }
        }).catch(err => {
          self.$message.error('创建失败 ：' + err)
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
