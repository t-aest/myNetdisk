<template>
  <div>
    <el-container style="height: 100%" direction="vertical">
      <el-header>
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item disabled style="opacity:1" index="1">myNetdisk</el-menu-item>
          <el-menu-item index="2">网盘</el-menu-item>
          <el-menu-item index="3">通讯录</el-menu-item>
          <el-menu-item index="3">历史记录</el-menu-item>
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
            <el-footer>
<!--              <el-progress :percentage="50" "></el-progress>-->
              <el-progress type="circle" :percentage="100" stroke-width="25" color="#ebeef5"></el-progress><br>
              <label>内存占用：</label><span>4156G/10T</span><br>
              <label>当前时间：</label><br><span>2021/07/11 21:13:02</span>

            </el-footer>
          </el-menu>
        </el-aside>
        <el-container>
          <el-main>
            <el-dropdown style="padding-right: 5px;padding-bottom: 3px">
              <el-button type="primary">
                <i class="el-icon-upload2 el-icon--left">上传</i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>上传文件</el-dropdown-item>
                <el-dropdown-item>上传文件夹</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-button-group>
              <el-button plain>
                <i class="el-icon-upload2 el-icon--left">新建文件夹</i>
              </el-button>
              <el-button plain>
                <i class="el-icon-upload2 el-icon--left">分享</i>
              </el-button>
              <el-button plain>
                <i class="el-icon-upload2 el-icon--left">下载</i>
              </el-button>
              <el-button plain>
                <i class="el-icon-upload2 el-icon--left">删除</i>
              </el-button>
              <el-button plain>
                <i class="el-icon-upload2 el-icon--left">重命名</i>
              </el-button>
              <el-button plain>
                <i class="el-icon-upload2 el-icon--left">复制到</i>
              </el-button>
              <el-button plain>
                <i class="el-icon-upload2 el-icon--left">移动到</i>
              </el-button>
            </el-button-group>

            <el-breadcrumb separator-class="el-icon-arrow-right">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>活动管理</el-breadcrumb-item>
              <el-breadcrumb-item>活动列表</el-breadcrumb-item>
              <el-breadcrumb-item>活动详情</el-breadcrumb-item>
            </el-breadcrumb>
            <el-table
              ref="multipleTable"
              :data="tableData"
              tooltip-effect="dark"
              style="width: 100%"
              @cell-mouse-enter="onMouseEnter"
              @selection-change="handleSelectionChange">
              <el-table-column
                type="selection"
                width="55">
              </el-table-column>
              <el-table-column
                label="日期"
                width="120">
                <template slot-scope="scope">{{ scope.row.date }}</template>
              </el-table-column>
              <el-table-column
                prop="name"
                label="姓名"
                width="120">
              </el-table-column>
              <el-table-column
                prop="address"
                label="地址"
                show-overflow-tooltip>
              </el-table-column>
            </el-table>
          </el-main>
          <el-footer>版权所有 &copy; xxxxxxxx &nbsp;&nbsp;  24小时客户服务热线：400-8879-597</el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>
<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: 'display content',
      tabPosition: 'left',
      tableData: [{
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-08',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-06',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-07',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }]
    }
  },
  methods: {
    onMouseEnter (row, column, cell, event) {
      console.log(row)
      console.log(column)
    }
  }
}
</script>
<style scoped>
.el-dropdown {
  vertical-align: top;
}
.el-table{
  border-top: solid 1px #e6e6e6;
}
.el-breadcrumb{
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
.el-menu{
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
</style>
