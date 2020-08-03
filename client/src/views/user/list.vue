<template>
  <div class="app-container">
    <!--列表开始-->
    <div class="filter-container">
      <el-input
        v-model="query.username"
        style="width: 13%;"
        placeholder="请输入用户名"
      />
      <el-input
        v-model="query.name"
        style="width: 13%;"
        placeholder="请输入所属机构"
      />
	  <!-- <el-select v-model="query.roleid" placeholder="请选择所属角色 " >
        <el-option
		      v-for="item in roleList"
		      :key="item.id"
		      :label="item.rolename"
		      :value="item.id">
		    </el-option>
	  </el-select> -->
    <el-select v-model="query.isroot" placeholder="请选择用户账号等级 " clearable="">
        <el-option value="1" label="管理员" v-if="isroot===1">管理员</el-option>
        <el-option value="2" label="行社" v-if="isroot===1 ||isroot===2">行社</el-option>
        <el-option value="3" label="网点" v-if="isroot===1 ||isroot===2 ||isroot===3">网点</el-option>
        <el-option value="4" label="商户">商户</el-option>
	  </el-select>

    <!-- <el-select v-model="query.rootid" placeholder="请选择所属 " >
        <el-option
		      v-for="item in clubList"
		      :key="item.id"
		      :label="item.clubname"
		      :value="item.id">
		    </el-option>
        <el-option
		      v-for="item in branchList"
		      :key="item.id"
		      :label="item.branchname"
		      :value="item.id">
		    </el-option>
        <el-option
		      v-for="item in merchList"
		      :key="item.id"
		      :label="item.merchname"
		      :value="item.id">
		    </el-option>
	  </el-select> -->
      <el-button type="primary" icon="el-icon-search" @click="fetchData" >
        查询
      </el-button>
      <el-button type="danger" icon="el-icon-search" @click="handleReset">
        重置
      </el-button>
      <el-button v-waves class="filter-item" type="success" icon="el-icon-plus"
      @click="editData()">新增
      </el-button>

    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="序号" fixed="left" type="index" :index="indexMethod" align="center" width="65"></el-table-column>
      <!-- <el-table-column label="ID" align="center">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column> -->
      <el-table-column label="用户名" align="center">
        <template slot-scope="scope">
          {{ scope.row.username }}
        </template>
      </el-table-column>
      
      <el-table-column label="用户角色" align="center">
        <template slot-scope="scope">
          {{ scope.row.rolename }}
        </template>
      </el-table-column>

      <el-table-column label="用户账号等级" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.isroot === 1">管理员</span>
          <span v-if="scope.row.isroot === 2">行社</span>
          <span v-if="scope.row.isroot === 3">网点</span>
          <span v-if="scope.row.isroot === 4">商户</span>
        </template>
      </el-table-column>
      
      <el-table-column label="所属机构" align="center" >
        <template slot-scope="scope">
          <span v-if="scope.row.isroot === 1">管理员</span>
          <span v-if="scope.row.isroot === 2">{{ scope.row.clubname }}</span>
          <span v-if="scope.row.isroot === 3">{{ scope.row.branchname }}</span>
          <span v-if="scope.row.isroot === 4">{{ scope.row.merchantname }}</span>
        </template>
      </el-table-column>
      
       
      <el-table-column label="创建人" align="center">
        <template slot-scope="scope">
          {{ scope.row.createuser }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createtime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="用户状态" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.state === 1">正常</span>
          <span v-if="scope.row.state === 0">禁用</span>
        </template>
      </el-table-column>

      <!-- <el-table-column align="center" prop="created_at" label="修改时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.modifytime }}</span>
        </template>
      </el-table-column> -->

      <el-table-column label="操作" align="center" width="420px">
        <template slot-scope="scope">
          <!-- <el-button @click="editData(scope.row)" type="success" size="mini" icon="el-icon-plus">
            详情
          </el-button> -->
          <el-button @click="updateStatus(scope.row)" type="danger" size="mini" icon="el-icon-edit" v-if="scope.row.state === 1">
            停用
          </el-button>
					<el-button @click="updateStatus(scope.row)" type="success" size="mini" icon="el-icon-edit" v-if="scope.row.state === 0">
						启用
					</el-button>
          <el-button @click="editData(scope.row)" type="warning" size="mini" icon="el-icon-edit">
            编辑
          </el-button>
          <el-button @click="handleDelete(scope.row.id)" type="danger" size="mini" icon="el-icon-remove">
            删除
          </el-button>
          <el-button @click="resetPassword(scope.row.id)" type="primary" size="mini" icon="el-icon-edit">
		        重置密码
		    </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :total="total"
      :current-page="page"
      :page-sizes="[15, 20, 50, 100, 200, 300, 400]"
      style="margin-top: 8px;"
      layout="total, prev, pager, next, sizes"
      @size-change="sizeChange"
      @current-change="pageChange"
    />

    <!-- 编辑-->
    <el-dialog id="editForm" :title="editForm.title" :center="true" :visible.sync="editForm.show">
    	<el-form  :rules="rules" status-icon :model="editForm" ref="editForm" style="max-width: 880px;margin: 0 auto;" label-position="right" label-width="80px" >
       <el-row :gutter="100">
          <el-col :lg="24">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="editForm.username" placeholder="" ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- <el-row :gutter="100">
          <el-col :lg="24">
            <el-form-item label="昵称">
              <el-input v-model="editForm.name" placeholder="" ></el-input>
            </el-form-item>
          </el-col>
        </el-row> -->

        <el-row :gutter="100">
          <el-col :lg="24">
            <el-form-item label="所属角色" prop="roleid">
              <el-select v-model="editForm.roleid" placeholder="请选择角色" style="width: 100%;" >
                <el-option 
                    v-for="item in roleList"
                    :key="item.id"
                    :label="item.rolename"
                    :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="100">
          <el-col :lg="24">
            <el-form-item label="账号等级" prop="isroot">
              <el-select v-model="editForm.isroot" placeholder="请选择账号权限" style="width: 100%;" @change="selectRole(editForm.isroot)">
               
                  <el-option value="1" label="管理员" v-if="isroot===1">管理员</el-option>
                  <el-option value="2" label="行社" v-if="isroot===1 ||isroot===2">行社</el-option>
                  <el-option value="3" label="网点" v-if="isroot===1 ||isroot===2 ||isroot===3">网点</el-option>
                  <el-option value="4" label="商户">商户</el-option>
                </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="100" v-if="editForm.isroot !== '1'">
          <el-col :lg="24" >
            <el-form-item label="所属机构" prop="rootid" >
              <el-select v-model="editForm.rootid" placeholder="请选择所属机构" style="width: 100%;">
                  <el-option 
                    v-for="item in clubList"
                    :key="item.id"
                    :label="item.clubname"
                    :value="item.id" >
                  </el-option>
                  <el-option  
                    v-for="item in branchList"
                    :key="item.id"
                    :label="item.branchname"
                    :value="item.id" >
                  </el-option>
                  <el-option 
                    v-for="item in merchList"
                    :key="item.id"
                    :label="item.merchname"
                    :value="item.id" >
                  </el-option>
                </el-select>
            </el-form-item>
          </el-col>
        </el-row>
    	<el-row :gutter="100">
    	  <el-col :lg="24">
    	    <el-form-item label="用户状态" prop="state">
    	      <el-radio-group v-model="editForm.state">
    	        <el-radio :label="1">启用</el-radio>
    	        <el-radio :label="0">停用</el-radio>
    	      </el-radio-group>
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
import { getpageList,getUserInfo,edit,del,updataUserState,selectRole ,resetPassword} from "@/api/user/user";

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
      branchList:[],
      merchList:[],
      roleList:[],
      clubList:[],
      query: {
        username: '',
        name: '',
        isroot: '',
        roleid:'',
        page: 1,
        limit: 15
      },
      formData: {
        DotName: "",
        DotAddress: ""
      },
      editForm:{
        show: false,
        id:"",
        username:"",
        name:"",
        state:'1',
        isroot:"",
        rootid:"",
        roleid:"",
		title:""
      },
      rules: {
        username: [
          { required: true, message: "请填写用户名", trigger: "blur" }
        ],
        isroot: [
          { required: true, message: "请选择账号权限", trigger: "change" }
        ],
        roleid: [
          { required: true, message: "请选择所属角色", trigger: "change" }
        ],
		rootid: [
		  { required: true, message: "请选择所属机构", trigger: "change" }
		],
		state: [
		  { required: true, message: "请选择状态", trigger: "change" }
		]
      }
    };
  },
  created() {
    this.fetchData();
  },

  methods: {
    fetchData() {
      this.listLoading = true;
      getpageList(this.query).then(response => {
        //debugger;
        this.list = response.data.records;
        this.total = parseInt(response.data.total);
        this.roleList = response.roleList;
        this.branchList = response.branchList;
        this.clubList = response.clubList;
        this.merchList = response.merchList;
        this.listLoading = false;
        this.isroot = response.isroot;
      });
    },
	//级联
    selectRole(isroot){
      this.$set(this.editForm,'rootid', '')
      selectRole({isroot:isroot}).then(response =>{
        this.branchList = response.branchList;
        this.clubList = response.clubList;
        this.merchList = response.merchList;
              
      })
    },
    //启用 禁用
	  updateStatus(row){
		this.$confirm('此操作将启用或停用账号, 是否继续?', '提示', {
		     confirmButtonText: '确定',
		     cancelButtonText: '取消',
		     type: 'warning'
		    }).then(() => {
		     updataUserState({
		         id: row.id,
		     	state: row.state
		     })
			 this.fetchData();
		    }).catch(() => {
		     this.$message({
		      type: 'info',
		      message: '已取消操作'
		     });
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
      this.query.username = "";
      this.query.name = "";
      this.query.isroot = "";
      this.fetchData();
    },
    //详情
    editData(row){
      this.editForm.show = true;
	    this.editForm.title = "新增用户";
      this.editForm.id = "";
      this.editForm.username = "";
      this.editForm.isroot = "";
      this.editForm.state = 1;
      this.editForm.rootid = "";
      this.editForm.roleid = "";
	this.branchList = "";
	this.clubList = "";
	this.merchList = "";
	this.$nextTick(() => {
          this.$refs.editForm.clearValidate();
          this.$refs.editForm.resetFields();
        })
      if (!row) {
          return;
      }
      getUserInfo({id: row.id}).then(res => {
		this.editForm.title = "编辑用户";
      	this.editForm.id = res.user.id;
        this.editForm.username = res.user.username;
        this.editForm.isroot = res.user.isroot+"";
        this.editForm.state = res.user.state;
        this.editForm.roleid = res.user.roleid;
      //   selectRole({isroot:this.editForm.isroot}).then(response =>{
      //   this.branchList = response.branchList;
      //   this.clubList = response.clubList;
      //   this.merchList = response.merchList;
      // })
	  this.selectRole(this.editForm.isroot);
        if (res.user.branchid!=null) {
          this.editForm.rootid = res.user.branchid;
        } 
        else if(res.user.clubid!=null){
          this.editForm.rootid = res.user.clubid;
        }
        else if(res.user.merchantid!=null){
          this.editForm.rootid = res.user.merchantid;
        }
      });
    },
    //取消
    cancel(){
      this.editForm.show = false;
    },
    //提交表单
    onSubmit(editForm) {
        this.$refs[editForm].validate((valid) => {
            if (valid) {
                edit(this.editForm).then(res => {
                    this.$notify({
                        title: '提示',
                        message: '提交成功',
                        type: 'success'
                    });
                    this.fetchData();
                    this.editForm.show = false;
                })
            } else {
                return false;
            }
        })
    },
    //删除用户
    handleDelete(id) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                del({ids: id}).then(res => {
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
    },
    //重置用户密码
    resetPassword(id) {
        this.$confirm('是否要重置此账号密码?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                resetPassword({ids: id}).then(res => {
                    this.$message({
                        type: 'success',
                        message: '重置成功!'
                    });
                    this.fetchData();
                })
            }).catch(() => {
                this.$message({
                type: 'info',
                message: '已取消重置'
            });
        });
    }

  }
};
</script>
