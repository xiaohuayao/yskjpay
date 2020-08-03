<template>
  <div class="app-container">
    <!--列表开始-->
    <div class="filter-container">
      <el-input v-model="query.clubname" style="width: 13%;" placeholder="请输入行社名称" />
      <el-input v-model="query.principal" style="width: 13%;" placeholder="请输入负责人" />
			
      <el-button type="primary" icon="el-icon-search" @click="fetchData(1)">
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
      <el-table-column label="行社名称" align="center">
        <template slot-scope="scope">
          {{ scope.row.clubname }}
        </template>
      </el-table-column>
      <el-table-column label="负责人" align="center">
        <template slot-scope="scope">
          {{ scope.row.principal }}
        </template>
      </el-table-column>
      <el-table-column label="联系电话" align="center">
        <template slot-scope="scope">
          {{ scope.row.phone }}
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.message }}</span>
        </template>
      </el-table-column>
	  
      <el-table-column label="操作" align="center" width="300px">
        <template slot-scope="scope">
  
          <el-button class="filter-item" @click="editData(scope.row)" type="warning" size="mini" icon="el-icon-edit">
            编辑
          </el-button>
          <el-button class="filter-item" @click="handleDelete(scope.row.id)" type="danger" size="mini" icon="el-icon-remove">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination :total="total" :current-page="page" :page-sizes="[15, 20, 50, 100, 200, 300, 400]" style="margin-top: 8px;"
      layout="total, prev, pager, next, sizes" @size-change="sizeChange" @current-change="pageChange" />

    <!-- 编辑-->
    <el-dialog id="editForm" title="详情" :center="true" :visible.sync="editForm.show">
      <el-form :rules="rules" status-icon :model="editForm" ref="editForm" style="max-width: 880px;margin: 0 auto;"
        label-position="right" label-width="80px">
        <el-row :gutter="100">
          <el-col :lg="24">
            <el-form-item label="行社名称" prop="clubname">
              <el-input v-model="editForm.clubname" placeholder="请输入行社名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="100">
          <el-col :lg="24">
            <el-form-item label="负责人" prop="principal">
              <el-input v-model="editForm.principal" placeholder="请输入负责人姓名"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="100">
          <el-col :lg="24">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="editForm.phone" placeholder="请输入负责人联系电话"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
		<el-row :gutter="100">
		  <el-col :lg="24">
		    <el-form-item label="备注">
		      <el-input v-model="editForm.message" placeholder="请输入备注"></el-input>
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
    getpageList,edit,del,info
  } from "@/api/club/index";

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
        query: {
          clubname: "",
          principal: "",
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
          clubname: "",
          principal: "",
          phone: "",
          message: ""
        },
        rules: {
          clubname: [{
            required: true,
            message: "请填写行社名称",
            trigger: "blur"
          }],
			principal: [{
			  required: true,
			  message: "请填写负责人",
			  trigger: "blur"
			}],
          phone: [{
            required: true,
            message: "请填写负责人电话",
            trigger: "blur"
          }]
        }
      };
    },
    created() {
      this.fetchData();
    },

    methods: {
      fetchData(type) {
		if(type==1){
			this.query.page=1;
		}
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
      handleReset(type) {
		if(type==1){
			this.query.page=1;
		}
        this.query.principal = "";
        this.query.clubname = "";
        this.fetchData();
      },
      //详情
      editData(row) {
        this.editForm.show = true;
		this.editForm.id = "";
		this.editForm.clubname = "";
		this.editForm.principal = "";
		this.editForm.phone = "";
		this.editForm.message = "";
        this.$nextTick(() => {
          this.$refs.editForm.clearValidate();
          this.$refs.editForm.resetFields();
        })
        if (!row) {
          return;
        }
        info({
          id: row.id
        }).then(res => {
          this.editForm.id = res.payClub.id;
          this.editForm.clubname = res.payClub.clubname;
          this.editForm.principal = res.payClub.principal;
          this.editForm.phone = res.payClub.phone;
          this.editForm.message = res.payClub.message;
        });
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
      //取消
      cancel() {
        this.editForm.show = false;
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
	  }
    }
  };
</script>
