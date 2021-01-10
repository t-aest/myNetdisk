<template>
  <div>
    <el-container style="height: 100%" direction="vertical">
      <el-header>
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item disabled style="opacity:1" index="1">myNetdisk</el-menu-item>
          <el-menu-item index="2">网盘</el-menu-item>
          <el-menu-item index="3">消息中心</el-menu-item>
        </el-menu>
      </el-header>
      <el-container>
        <el-aside width="200px" style="text-align: center;border: solid 1px #e6e6e6">
          <el-menu :default-openeds="['1', '3']" mode="vertical">
            <el-submenu index="1">
              <template slot="title"><i class="el-icon-message"></i>导航一</template>
              <el-menu-item-group>
                <template slot="title">分组一</template>
                <el-menu-item index="1-1">选项1</el-menu-item>
                <el-menu-item index="1-2">选项2</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="分组2">
                <el-menu-item index="1-3">选项3</el-menu-item>
              </el-menu-item-group>
              <el-submenu index="1-4">
                <template slot="title">选项4</template>
                <el-menu-item index="1-4-1">选项4-1</el-menu-item>
              </el-submenu>
            </el-submenu>
            <el-submenu index="2">
              <template slot="title"><i class="el-icon-menu"></i>导航二</template>
              <el-menu-item-group>
                <template slot="title">分组一</template>
                <el-menu-item index="2-1">选项1</el-menu-item>
                <el-menu-item index="2-2">选项2</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="分组2">
                <el-menu-item index="2-3">选项3</el-menu-item>
              </el-menu-item-group>
              <el-submenu index="2-4">
                <template slot="title">选项4</template>
                <el-menu-item index="2-4-1">选项4-1</el-menu-item>
              </el-submenu>
            </el-submenu>
            <el-submenu index="3">
              <template slot="title"><i class="el-icon-setting"></i>导航三</template>
              <el-menu-item-group>
                <template slot="title">分组一</template>
                <el-menu-item index="3-1">选项1</el-menu-item>
                <el-menu-item index="3-2">选项2</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="分组2">
                <el-menu-item index="3-3">选项3</el-menu-item>
              </el-menu-item-group>
              <el-submenu index="3-4">
                <template slot="title">选项4</template>
                <el-menu-item index="3-4-1">选项4-1</el-menu-item>
              </el-submenu>
            </el-submenu>
          </el-menu>
<!--          <el-tabs :tab-position="tabPosition">-->
<!--            <el-tab-pane>-->
<!--              <span slot="label"><i class="el-icon-folder" disabled="true"></i>&nbsp 全部文件</span>-->
<!--            </el-tab-pane>-->
<!--            <el-tab-pane label="图片"></el-tab-pane>-->
<!--            <el-tab-pane label="文档"></el-tab-pane>-->
<!--            <el-tab-pane label="视频"></el-tab-pane>-->
<!--            <el-tab-pane label="种子"></el-tab-pane>-->
<!--            <el-tab-pane label="音乐"></el-tab-pane>-->
<!--            <el-tab-pane label="其它"></el-tab-pane>-->
<!--            <el-tab-pane label="我的分享">-->
<!--              <span slot="label"><i class="el-icon-share" disabled="true"></i>&nbsp 我的分享</span>-->
<!--            </el-tab-pane>-->
<!--            <el-tab-pane label="回收站">-->
<!--              <span slot="label"><i class="el-icon-delete" disabled="true"></i>&nbsp 回收站</span>-->
<!--            </el-tab-pane>-->
<!--            <el-tab-pane label="回收站">-->
<!--              <span slot="label"><el-progress :percentage="100" :format="format"></el-progress></span>-->
<!--            </el-tab-pane>-->
<!--          </el-tabs>-->
        </el-aside>
        <el-container>
          <el-main>
            <el-button size="small" type="primary">点击上传</el-button>
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
          <el-footer>Footer</el-footer>
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
  }
}
</script>
<style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
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
  color: #333;
  float: left;
  height: calc(100vh - 60px);
}
.el-main {
  color: #333;
}
</style>
