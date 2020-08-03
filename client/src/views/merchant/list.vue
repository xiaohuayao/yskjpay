<template>
	<div class="app-container">
		<!--列表开始-->
		<div class="filter-container">
			<el-input v-model="query.merchantname" style="width: 13%;" placeholder="请输入商户名称" />
			<el-input v-model="query.swiffpassmerchno" style="width: 13%;" placeholder="请输入威富通商户号" />
			<el-input v-model="query.merid" style="width: 13%;" placeholder="请输入统一收单号" />
			<el-input v-model="query.clubname" style="width: 13%;" placeholder="请输入行社名称" />
			<el-input v-model="query.branchname" style="width: 13%;" placeholder="请输入网点名称" />


			<el-button type="primary" icon="el-icon-search" @click="fetchData(1)">
				查询
			</el-button>
			<el-button type="danger" icon="el-icon-search" @click="handleReset">
				重置
			</el-button>
			<el-button v-waves class="filter-item" type="success" icon="el-icon-plus" @click="editData()" v-if="  isroot == 1  ||  isroot == 2 ||  isroot == 3">
				新增
			</el-button>

		</div>
		<el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
			<el-table-column label="序号" fixed="left" type="index" :index="indexMethod" align="center" width="65"></el-table-column>

			<el-table-column label="商户名称" align="center" >
				<template slot-scope="scope">
					<div>{{ scope.row.merchname }}</div>
				</template>
			</el-table-column>
			<el-table-column label="威富通商户号" align="center">
				<el-table-column label="统一收单账号" align="center" :show-overflow-tooltip='true' width="160">
					<template slot-scope="scope">
						<div>{{ scope.row.swiffpassmerchno }}</div>
						<hr style="height:1px;border:none;border-top:1px solid #E4E4E4;width: 140%;margin-left: -10%;" />
						<div>{{ scope.row.merid }}</div>
					</template>
				</el-table-column>
			</el-table-column>

			<el-table-column label="支付通道" align="center">
				<template slot-scope="scope">
					<span v-if="scope.row.openchannel === 1">威富通</span>
					<span v-if="scope.row.openchannel === 2">统一收单</span>
				</template>
			</el-table-column>
			
			<el-table-column label="商户标识" align="center">
				<template slot-scope="scope">
					<span>{{ scope.row.merinst }}</span>
				</template>
			</el-table-column>

			<el-table-column label="商户状态" align="center">
				<template slot-scope="scope">
					<span v-if="scope.row.status === 1">启用</span>
					<span v-if="scope.row.status === 0">停用</span>
				</template>
			</el-table-column>

			<el-table-column label="扣点信息" align="center">
				<template slot-scope="scope">
					<span>{{ scope.row.bucklepoint }}</span>
				</template>
			</el-table-column>
			<el-table-column label="最后一笔交易时间" align="center" :show-overflow-tooltip='true'>
				<template slot-scope="scope">
					<div>{{handleDate(scope.row.lastpaytime)}}</div>
					<div>{{handleTime(scope.row.lastpaytime)}}</div>
				</template>
			</el-table-column>
			<el-table-column label="所属行社" align="center">
				<template slot-scope="scope">
					<span>{{ scope.row.clubname }}</span>
				</template>
			</el-table-column>
			<el-table-column label="所属网点" align="center">
				<template slot-scope="scope">
					<span>{{ scope.row.branchname }}</span>
				</template>
			</el-table-column>
			<el-table-column label="创建人" align="center">
				<template slot-scope="scope">
					<span>{{ scope.row.createuser }}</span>
				</template>
			</el-table-column>
			<el-table-column label="创建时间" align="center" :show-overflow-tooltip='true'>
				<template slot-scope="scope">
					<div>{{handleDate(scope.row.createtime)}}</div>
					<div>{{handleTime(scope.row.createtime)}}</div>
				</template>
			</el-table-column>


			<el-table-column label="操作" align="center" width="300px">
				<template slot-scope="scope">


					<el-button @click="updataStatus(scope.row)" type="danger" size="mini" icon="el-icon-edit" v-if="scope.row.status === 1">
						停用
					</el-button>
					<el-button @click="updataStatus(scope.row)" type="success" size="mini" icon="el-icon-edit" v-if="scope.row.status === 0">
						启用
					</el-button>

					<el-button @click="updataMerchantOpenChannel(scope.row)" type="primary" size="mini">
						切换通道
					</el-button>
					<el-button @click="editData(scope.row)" type="warning" size="mini" icon="el-icon-edit">
						配置
					</el-button>
					
					<!-- <el-button @click="handleDelete(scope.row.id)" type="danger" size="mini" icon="el-icon-remove">
		    移除
		  </el-button> -->


				</template>
			</el-table-column>
		</el-table>

		<el-pagination :total="total" :current-page="page" :page-sizes="[15, 20, 50, 100, 200, 300, 400]" style="margin-top: 8px;"
		 layout="total, prev, pager, next, sizes" @size-change="sizeChange" @current-change="pageChange" />

		<!-- 编辑-->
		<el-dialog id="editForm" :title="editForm.title" :center="true" :visible.sync="editForm.show">
			<el-form :rules="rules" status-icon :model="editForm" ref="editForm" style="max-width: 880px;margin: 0 auto;"
			 label-position="right" label-width="120px">
			    <el-row :gutter="100">
			    	<el-col :lg="24">
			    		<el-form-item label="商户名称" prop="merchname">
			    			<el-input v-model="editForm.merchname" placeholder=""></el-input>
			    		</el-form-item>
			    	</el-col>
			    </el-row>
				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="应用密钥" prop="appkey">
							<el-input v-model="editForm.appkey" placeholder=""></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="支付通道" prop="openchannel">
							<el-radio-group v-model="editForm.openchannel">
								<el-radio :label="1">威富通</el-radio>
								<el-radio :label="2">统一收单</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="威富通商户号" prop="swiffpassmerchno">
							<el-input v-model="editForm.swiffpassmerchno" placeholder=""></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="威富通商户密钥" prop="swiffpasspaysecert">
							<el-input v-model="editForm.swiffpasspaysecert" placeholder=""></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				
				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="统一收单账号" prop="merid">
							<el-input v-model="editForm.merid" placeholder=""></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="商户标识" prop="merinst">
							<el-input v-model="editForm.merinst" placeholder=""></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="证书密钥" prop="certpwd">
							<el-input v-model="editForm.certpwd" placeholder=""></el-input>
						</el-form-item>
					</el-col>
				</el-row>



				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="上传证书">
							<el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" :on-preview="handlePreview"
							 :on-remove="handleRemove" :before-remove="beforeRemove" multiple :limit="1" :on-exceed="handleExceed" ref="upload"
							 :file-list="fileList" :http-request="getFile" :beforeUpload="beforeAvatarUpload">
								<el-button size="small" type="primary">选择文件</el-button>
								<div slot="tip" class="el-upload__tip">只能上传pfx文件，且不超过10MB</div>
							</el-upload>
							<el-button size="small" type="success" @click="upload">确认上传</el-button>

						</el-form-item>
					</el-col>
				</el-row>


				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="证书存储位置" prop="certpath">
							<el-input v-model="editForm.certpath" placeholder="" :disabled="true"></el-input>
						</el-form-item>
					</el-col>
				</el-row>



				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="商户状态" prop="bucklepoint">
							<el-radio-group v-model="editForm.status">
								<el-radio :label="1">启用</el-radio>
								<el-radio :label="0">停用</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="扣点信息" prop="bucklepoint">
							<el-input v-model="editForm.bucklepoint" placeholder=""></el-input>
						</el-form-item>
					</el-col>
				</el-row>


				<el-row :gutter="100" v-if="  isroot == 1  ||  isroot == 2 ||  isroot == 3">
					<el-col :lg="24">
						<el-form-item label="所属网点:" prop="branchid">
							<el-select v-model="editForm.branchid" style="width: 100%;" filterable placeholder="请选择网点">
								<el-option v-for="item in editForm.branchlist" :key="item.id" :label="item.branchname" :value="item.id">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="创建人" v-if="editForm.createuser">
							<el-input v-model="editForm.createuser" placeholder="" :disabled="true"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="创建时间" v-if="editForm.createtime">
							<el-input v-model="editForm.createtime" placeholder="" :disabled="true"></el-input>
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
		getpageList,
		getMerchantInfo,
		updataMerchantStatus,
		updataMerchantOpenChannel,
		branchlist,
		edit,
		del,
		uploadFile,
	} from "@/api/merchant/merchant";

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
				isroot: "",

				//文件上传
				file: {},
				fileList: [],

				query: {
					merchantname: "",
					swiffpassmerchno: "",
					merid: "",
					clubname: "",
					branchname: "",
					page: 1,
					limit: 15
				},
				formData: {
					DotName: "",
					DotAddress: ""
				},
				editForm: {
					title: "",
					show: false,
					id: "",
					appkey: "",
					merchname: "",
					swiffpassmerchno: "",
					swiffpasspaysecert: "",
					openchannel: 2,
					merid: "",
					merinst: "",
					certpath: "",
					certpwd: "",
					createuser: "",
					createtime: "",
					status: 1,
					clubname: "",
					branchname: "",
					bucklepoint: "",
					lastpaytime: "",
					branchid: "",
					clublist: [],
					branchlist: [],
				},
				rules: {
					merchname: [{
						required: true,
						message: "请填写商户名称",
						trigger: "blur"
					}],
					appkey: [{
						required: true,
						message: "请填写商户密钥",
						trigger: "blur"
					}],
					swiffpassmerchno: [{
						required: true,
						message: "请填写威富通商户号",
						trigger: "blur"
					}],
					swiffpasspaysecert: [{
						required: true,
						message: "请填写威富通商户密钥",
						trigger: "blur"
					}],
					merid: [{
						required: true,
						message: "请填写统一收单账号",
						trigger: "blur"
					}],
					merinst: [{
						required: true,
						message: "请填写商户标识",
						trigger: "blur"
					}],
					certpwd: [{
						required: true,
						message: "请填写商户标识",
						trigger: "blur"
					}],
					bucklepoint: [{
						required: true,
						message: "请填写商户扣点信息",
						trigger: "blur"
					}],
					branchid: [{
						required: true,
						message: "请选择商户所属网点",
						trigger: "blur"
					}],

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
					this.isroot = response.isroot;
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
				this.query.merchantname = "";
				this.query.swiffpassmerchno = "";
				this.query.merid = "";
				this.query.clubname = "";
				this.query.branchname = "";
				this.fetchData();
			},
			//启用 禁用
			updataStatus(row) {
				this.$confirm('此操作将启用或停用商户, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					updataMerchantStatus({
						id: row.id,
						status: row.status
					})
					this.fetchData();
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消操作'
					});
				});
			},
			handleDate(timestamp){
				if(timestamp==undefined){
					return "";
				}
				var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
				var Y = date.getFullYear() + '-';
				var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
				var D = (date.getDate() < 10 ? '0'+date.getDate() : date.getDate());
				return Y+M+D;
			},
			handleTime(timestamp){
				if(timestamp==undefined){
					return "";
				}
				var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
				var h = (date.getHours() < 10 ? '0'+date.getHours() : date.getHours()) + ':';
				var m = (date.getMinutes() < 10 ? '0'+date.getMinutes() : date.getMinutes()) + ':';
				var s = (date.getSeconds() < 10 ? '0'+date.getSeconds() : date.getSeconds());
				return h+m+s;
			},

			//切换商户支付通道
			updataMerchantOpenChannel(row) {
				this.$confirm('此操作将切换商户支付通道, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					updataMerchantOpenChannel({
						id: row.id,
						openchannel: row.openchannel
					})
					this.fetchData();
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消操作'
					});
				});

			},


			//详情
			editData(row) {
				//文件上传
				this.file = {};
				this.fileList = [];

				this.editForm.title = "添加商户";
				this.editForm.show = true;
				this.editForm.id = "";
				this.editForm.appkey = "";
				this.editForm.merchname = "";
				this.editForm.swiffpassmerchno = "";
				this.editForm.swiffpasspaysecert = "";
				this.editForm.openchannel = 2;
				this.editForm.merid = "";
				this.editForm.merinst = "";
				this.editForm.certpath = "";
				this.editForm.certpwd = "";
				this.editForm.createuser = "";
				this.editForm.createtime = "";
				this.editForm.amount = "";
				this.editForm.status = 1;
				this.editForm.branchname = "";
				this.editForm.clubname = "";
				this.editForm.bucklepoint = "";
				this.editForm.lastpaytime = "";
				this.editForm.branchid = "";
				this.editForm.branchlist = [];

				branchlist().then(res => {
					this.editForm.branchlist = res.data;
				});

				if (!row) {
					return;
				}
				getMerchantInfo({
					id: row.id
				}).then(res => {
					this.editForm.title = "编辑商户";
					this.editForm.id = res.payMerchant.id;
					this.editForm.appkey = res.payMerchant.appkey;
					this.editForm.merchname = res.payMerchant.merchname;
					this.editForm.swiffpassmerchno = res.payMerchant.swiffpassmerchno;
					this.editForm.swiffpasspaysecert = res.payMerchant.swiffpasspaysecert;
					this.editForm.openchannel = res.payMerchant.openchannel;
					this.editForm.merid = res.payMerchant.merid;
					this.editForm.merinst = res.payMerchant.merinst;
					this.editForm.certpath = res.payMerchant.certpath;
					this.editForm.certpwd = res.payMerchant.certpwd;
					this.editForm.createuser = res.payMerchant.createuser;
					this.editForm.createtime = res.payMerchant.createtime;
					this.editForm.amount = res.payMerchant.amount;
					this.editForm.status = res.payMerchant.status;
					this.editForm.clubname = res.payMerchant.clubname;
					this.editForm.branchname = res.payMerchant.branchname;
					this.editForm.bucklepoint = res.payMerchant.bucklepoint;
					this.editForm.lastpaytime = res.payMerchant.lastpaytime;
					this.editForm.branchid = res.payMerchant.branchid;

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
			// //删除用户
			// handleDelete(id) {
			//     this.$confirm('此操作将永久删除, 是否继续?', '提示', {
			//             confirmButtonText: '确定',
			//             cancelButtonText: '取消',
			//             type: 'warning'
			//         }).then(() => {
			//             del({ids: id}).then(res => {
			//                 this.$message({
			//                     type: 'success',
			//                     message: '删除成功!'
			//                 });
			//                 this.fetchData();
			//             })
			//         }).catch(() => {
			//             this.$message({
			//             type: 'info',
			//             message: '已取消删除'
			//         });
			//     });
			// },

			//文件上传的方法
			//清空
			removeData() {
				this.file = {};
				this.fileList = [];
				this.$refs.upload.clearFiles();
			},
			handleRemove(file, fileList) {
				console.log(file, fileList);
				this.removeData();
			},
			handlePreview(file) {
				console.log(file);
			},
			handleExceed(files, fileList) {
				// this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
				this.$message.warning("只能上传一个文件");
			},
			beforeRemove(file, fileList) {
				return this.$confirm(`确定移除 ${ file.name }？`);
			},

			//上传文件限制
			beforeAvatarUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				const extension = testmsg === 'pfx'
				const extension2 = testmsg === 'PFX'
				const isLt2M = file.size / 1024 / 1024 < 10
				if (!extension && !extension2) {
					this.removeData();
					this.$message({
						message: '上传文件只能是 pfx、PFX格式!',
						type: 'warning'
					});
				}
				if (!isLt2M) {
					this.removeData();
					this.$message({
						message: '上传文件大小不能超过 10MB!',
						type: 'warning'
					});
				}
			},

			//文件上传
			getFile(item) {
				console.log(item.file)
				this.file = item.file
			},
			upload() {
				if (this.file.name == undefined) {
					this.$message.warning('请选择文件');
					return;
				}
				var testmsg = this.file.name.substring(this.file.name.lastIndexOf('.') + 1)
				const extension = testmsg === 'pfx'
				const extension2 = testmsg === 'PFX'
				const isLt2M = this.file.size / 1024 / 1024 < 1
				if (!extension && !extension2) {
					this.$message.warning('请选择文件');
					return;
				}
				if (!isLt2M) {
					this.$message.warning('请选择文件');
					return;
				}

				const fd = new FormData()
				fd.append('file', this.file)
				const config = {
					headers: {
						'Content-Type': 'multipart/form-data'
					}
				}
				uploadFile(fd, config).then(response => {
					this.editForm.certpath = response.filePath
				})
				this.removeData();
			},

		},
	};
</script>

<style>
	.el-table__header tr,
	  .el-table__header th {
	    padding: 0;
	    height: 40px;
	}
	.el-table__body tr,
	  .el-table__body td {
	    padding: 40;

	}
</style>
