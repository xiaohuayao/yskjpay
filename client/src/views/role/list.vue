<template>
  <div class="app-container">
    <!--列表开始-->
    <div class="filter-container">
      <el-input v-model="query.rolename" style="width: 13%;" placeholder="请输入权限名称" />
      <!-- <el-input v-model="query.principal" style="width: 13%;" placeholder="请输入负责人" /> -->
      <!-- <el-select v-model="query.organId" placeholder="请选择部门">
        <el-option
          v-for="item in orgList"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select> -->
      <el-button type="primary" icon="el-icon-search" @click="fetchData">
        查询
      </el-button>
      <el-button type="danger" icon="el-icon-search" @click="handleReset">
        重置
      </el-button>
      <el-button v-waves class="filter-item" type="success" icon="el-icon-plus" @click="editData()">
        新增
      </el-button>
    </div>
    <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
      <el-table-column label="序号" fixed="left" type="index" :index="indexMethod" align="center" width="65"></el-table-column>
      <el-table-column label="角色名称" align="center">
        <template slot-scope="scope">
          {{ scope.row.rolename }}
        </template>
      </el-table-column>
      <el-table-column label="角色说明" align="center">
        <template slot-scope="scope">
          {{ scope.row.remarks }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.createtime }}
        </template>
      </el-table-column>
	  
      <el-table-column label="操作" align="center" width="300px">
        <template slot-scope="scope">
          <!-- <el-button @click="editData(scope.row)" type="success" size="mini" icon="el-icon-plus">
            详情
          </el-button> -->
          <el-button class="filter-item" @click="editData(scope.row)" type="warning" size="mini" icon="el-icon-edit">
            编辑
          </el-button>
          <el-button class="filter-item" @click="handleDelete(scope.row)" type="danger" size="mini" icon="el-icon-remove">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :total="total" :current-page="page" :page-sizes="[15, 20, 50, 100, 200, 300, 400]" style="margin-top: 8px;"
      layout="total, prev, pager, next, sizes" @size-change="sizeChange" @current-change="pageChange" />

    <!-- 编辑-->
    <el-dialog id="editForm" :title="title" :center="true" :visible.sync="editForm.show">
      <el-form :rules="rules" status-icon :model="editForm" ref="editForm" style="max-width: 880px;margin: 0 auto;"
        label-position="right" label-width="80px">
        <el-row :gutter="100">
          <el-col :lg="24">
            <el-form-item label="角色名称" prop="rolename">
              <el-input v-model="editForm.rolename" placeholder="请输入角色名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="100">
          <el-col :lg="24">
            <el-form-item label="角色说明" prop="remarks">
              <el-input v-model="editForm.remarks" placeholder="请输入角色说明"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="100">
          <el-col :lg="24">
			  <el-form-item label="菜单权限" >
				  <el-tree
				    :data="menuOptions"
				    show-checkbox
				    default-expand-all
				    node-key="id"
				    ref="menu"
				    highlight-current
					empty-text="加载中，请稍后"
				    :props="defaultProps">
				  </el-tree>
			  </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
		<el-button type="primary" @click="onSubmit('editForm')" icon="el-icon-check">提交</el-button>
        <el-button type="primary" v-waves @click="cancel()" icon="el-icon-success">取消</el-button>
      </div>
    </el-dialog>
	
  </div>
</template>

<script>
  import {
    getpageList,listMenu,getTree,setTree,saveRole,del
  } from "@/api/role/role";

  export default {
    filters: {
      statusFilter(status) {
        const statusMap = {
          published: "success",
          draft: "gray",
          deleted: "danger"
        };
        return statusMap[status];
      }
    },
    data() {
      return {
        list: null,
        total: 0,
        listLoading: true,
        dialogFormVisible: false,
        titleText: "新增网点",
		title:"新增权限",
		menuOptions:[],
		defaultProps:{
		 children: "children",
		 label: "menuName"
		},
        query: {
          rolename: "",
          page: 1,
          limit: 15
        },
        formData: {
          DotName: "",
          DotAddress: ""
        },
        editForm: {
          show: false,
          id: "",
          rolename: "",
          remarks: "",
          menuIds: [],
		  title:""
        },
        rules: {
          menuIds: [{
            required: true,
            message: "请选择权限菜单",
            trigger: "blur"
          }],
		
        }
      };
    },
    created() {
      this.fetchData();
	  this.getMenuTreeselect();
    },

    methods: {
      fetchData() {
        this.listLoading = true;
        getpageList(this.query).then(response => {
          //debugger;
          this.list = response.data.records;
          this.total = parseInt(response.data.total);
          this.listLoading = false;
        });
      },
      // 改变页码
      pageChange(e) {
        //debugger;
        this.query.page = e;
        this.fetchData();
      },
      // 改变每页显示数
      sizeChange(e) {
        //debugger;
        this.query.page = 1;
        this.query.limit = e;
        this.fetchData();
      },
      //重置
      handleReset() {
        this.query.rolename = "";
        this.fetchData();
      },
	  /** 查询菜单树结构 */
	  getMenuTreeselect() {
		listMenu().then(response => {
		  this.menuOptions = response.data;
		});
	  },
      //编辑
      editData(row) {
		if (this.$refs.menu != undefined) {
		  this.$refs.menu.setCheckedKeys([]);
		}
		this.editForm.show = true;
		this.editForm.id = "";
		this.editForm.rolename = "";
		this.editForm.remarks = "";
		this.editForm.menuIds = [];
		this.title = "新增权限";
        this.$nextTick(() => {
          this.$refs.editForm.clearValidate();
          this.$refs.editForm.resetFields();
        })
		if(row){
			this.editForm.id = row.id;
			this.title = "修改权限";
		}
        getTree({
          id: row.id
        }).then(res => {
          this.editForm.rolename = res.role.rolename;
          this.editForm.remarks = res.role.remarks;
		  console.log(res.menuIds);
		  this.$nextTick(() => {
			  // this.$refs.tree.setCheckedNodes(res.menu);
			  this.$refs.menu.setCheckedKeys(res.menuIds);
		  })
        });
      },
	  //提交表单
	  onSubmit(editForm) {
	      this.$refs[editForm].validate((valid) => {
	          if (valid) {
				  // console.log(this.$refs.menu.getCheckedNodes(false, true))
				  // console.log(this.$refs.menu.getHalfCheckedKeys());//获取半选中父节点
				  // console.log(this.$refs.menu.getCheckedKeys());
				  let res = this.$refs.menu.getCheckedNodes(false, true); //这里两个true，1. 是否只是叶子节点 2. 是否包含半选节点（就是使得选择的时候不包含父节点）
				  let arr = [];
				  res.forEach(item => {
				      arr.push(item.id);
				  });
				  this.editForm.menuIds = JSON.stringify(arr);
				  // this.editForm.menuIds = JSON.stringify(this.$refs.menu.getCheckedKeys());
				
				  if(this.editForm.id){
					  setTree(this.editForm).then(res => {
					      this.$notify({
					          title: '提示',
					          message: '修改成功',
					          type: 'success'
					      });
					      this.fetchData();
					      this.editForm.show = false;
					  })
				  }else{
					  saveRole(this.editForm).then(res => {
					      this.$notify({
					          title: '提示',
					          message: '新增成功',
					          type: 'success'
					      });
					      this.fetchData();
					      this.editForm.show = false;
					  })
				  }
	          } else {
	              return false;
	          }
	      })
	  },
      //取消
      cancel() {
        this.editForm.show = false;
      },
	  //删除
	  handleDelete(row) {
	      this.$confirm('是否确认删除名称为"' + row.rolename + '"的数据项?', "警告", {
	              confirmButtonText: '确定',
	              cancelButtonText: '取消',
	              type: 'warning'
	          }).then(() => {
	              del({id: row.id}).then(res => {
	                  this.$message({
	                      type: 'success',
	                      message: '删除成功!'
	                  });
	                  this.fetchData();
	              })
	          }).catch(() => {
	              this.$message({
	              type: 'info',
	              message: '已取消删除'
	          });
	      });
	  }
    }
  };
</script>
