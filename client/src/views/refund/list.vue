<template>
	<div class="app-container">
		<!--列表开始-->
		<div class="filter-container">
			<el-input v-model="query.merchantName" style="width: 13%;" placeholder="请输入所属商户" />
			<el-input v-model="query.payorderno" style="width: 13%;" placeholder="请输入原交易订单号" />
			<el-date-picker v-model="time" type="datetimerange" :picker-options="pickerOptions" range-separator="至"
			 start-placeholder="开始日期" end-placeholder="结束日期" align="right" :default-time="['00:00:00', '23:59:59']">
			</el-date-picker>

			<el-button type="primary" icon="el-icon-search" @click="fetchData(1)">
				查询
			</el-button>
			<el-button type="danger" icon="el-icon-search" @click="handleReset">
				重置
			</el-button>
		</div>
		<el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row
		 @sort-change="changeSort">
			<el-table-column label="序号" fixed="left" type="index" :index="indexMethod" align="center" width="65"></el-table-column>
			<el-table-column label="所属商户" align="center">
				<template slot-scope="scope">
					{{ scope.row.merchantName }}
				</template>
			</el-table-column>
			<el-table-column label="退款订单号" align="center">
				<template slot-scope="scope">
					{{ scope.row.refundorderno }}
				</template>
			</el-table-column>
			<el-table-column label="原订单金额" align="center">
				<template slot-scope="scope">
					{{ scope.row.money }}
				</template>
			</el-table-column>
			<el-table-column label="退款金额" align="center">
				<template slot-scope="scope">
					{{ scope.row.refundamount }}
				</template>
			</el-table-column>
			<el-table-column label="退款类型" align="center">
				<template slot-scope="scope">
					{{ scope.row.paystateName }}
				</template>
			</el-table-column>
			<el-table-column label="退款时间" align="center" prop="refundTime" sortable="custom">
				<template slot-scope="scope">
					<div>{{handleDate(scope.row.refundtime)}}</div>
					<div>{{handleTime(scope.row.refundtime)}}</div>
				</template>
			</el-table-column>
			<el-table-column label="原交易订单号" align="center">
				<template slot-scope="scope">
					{{ scope.row.payorderno }}
				</template>
			</el-table-column>
			<el-table-column label="操作人" align="center">
				<template slot-scope="scope">
					{{ scope.row.operator }}
				</template>
			</el-table-column>
		</el-table>

		<el-pagination :total="total" :current-page="page" :page-sizes="[15, 20, 50, 100, 200, 300, 400]" style="margin-top: 8px;"
		 layout="total, prev, pager, next, sizes" @size-change="sizeChange" @current-change="pageChange" />

		<!-- 编辑-->

	</div>
</template>

<script>
	import {
		getpageList
	} from "@/api/refund/index";

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
				time: [],
				pickerOptions: {
					shortcuts: [{
						text: '今天',
						onClick(picker) {
							const end = new Date(new Date(new Date().toLocaleDateString()).getTime() + 24 * 60 * 60 * 1000 - 1);
							const start = new Date(new Date().toLocaleDateString());
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '昨天',
						onClick(picker) {
							const end = new Date(new Date(new Date().toLocaleDateString()).getTime() + 24 * 60 * 60 * 1000 - 1);
							const start = new Date(new Date().toLocaleDateString());
							start.setTime(start.getTime() - 3600 * 1000 * 24 * 1);
							end.setTime(end.getTime() - 3600 * 1000 * 24 * 1);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '本周',
						onClick(picker) {
							var timesStamp = new Date().getTime();
							var currenDay = new Date().getDay();

							var start = new Date(new Date(timesStamp + 24 * 60 * 60 * 1000 * (0 - (currenDay + 6) % 7)).toLocaleDateString());
							for (var i = 0; i < 7; i++) {
								var end = new Date(new Date(new Date(timesStamp + 24 * 60 * 60 * 1000 * (i - (currenDay + 6) % 7)).toLocaleDateString())
									.getTime() + 24 * 60 * 60 * 1000 - 1);
							}
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '本月',
						onClick(picker) {
							var now = new Date(); //当前日期
							var nowMonth = now.getMonth(); //当前月
							var nowYear = now.getFullYear(); //当前年
							//本月的开始时间
							var start = new Date(nowYear, nowMonth, 1);
							//本月的结束时间
							var end = new Date(new Date(new Date(nowYear, nowMonth + 1, 0).toLocaleDateString()).getTime() + 24 * 60 * 60 *
								1000 - 1);
							picker.$emit('pick', [start, end]);
						}
					}]
				},
				list: null,
				total: 0,
				listLoading: true,
				dialogFormVisible: false,
				titleText: "新增网点",
				query: {
					merchantName: "",
					payorderno: "",
					startTime: "",
					endTime: "",
					page: 1,
					limit: 15,
					sorts: 2,
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
					DotName: [{
						required: true,
						message: "请填写网点名称",
						trigger: "blur"
					}],
					DotAddress: [{
						required: true,
						message: "请填写网点地址",
						trigger: "blur"
					}]
				}
			};
		},
		created() {
			this.fetchData();
		},

		methods: {
			fetchData() {
				this.listLoading = true;

				//给时间查询条件赋值
				var arr = this.time;
				if (arr != undefined) {
					this.query.startTime = arr[0];
					this.query.endTime = arr[1];
					if (this.query.startTime != undefined && this.query.startTime != "") {
						var s = new Date(this.query.startTime);
						this.query.startTime = s.getFullYear() + '-' + (s.getMonth() + 1) + '-' + s.getDate() + ' ' + s.getHours() + ':' +
							s.getMinutes() + ':' + s.getSeconds()
					}
					if (this.query.endTime != undefined && this.query.endTime != "") {
						var e = new Date(this.query.endTime);
						this.query.endTime = e.getFullYear() + '-' + (e.getMonth() + 1) + '-' + e.getDate() + ' ' + e.getHours() + ':' +
							e
							.getMinutes() + ':' + e.getSeconds()
					}
				} else {
					this.query.startTime = "";
					this.query.endTime = "";
				}
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
				if (type == 1) {
					this.query.page = 1;
				}
				this.time = [];
				this.pageChange("1");
				this.query.merchantName = "";
				this.query.payorderno = "";
				this.query.startTime = "";
				this.query.endTime = "";
				this.query.sorts = 2;
				this.fetchData();
			},
			//排序
			changeSort(val) {
				console.log(val)
				let sorts = this.query.sorts;
				if (val.order == "ascending") {
					sorts = 1;
				} else {
					sorts = 2;
				}
				this.query.sorts = sorts;
				this.pageChange("1");
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
					del({
						ids: id
					}).then(res => {
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
			handleDate(timestamp) {
				if(timestamp==undefined){
					return "";
				}
				var date = new Date(timestamp); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
				var Y = date.getFullYear() + '-';
				var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
				var D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate());
				return Y + M + D;
			},
			handleTime(timestamp) {
				if(timestamp==undefined){
					return "";
				}
				var date = new Date(timestamp); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
				var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
				var m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
				var s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
				return h + m + s;
			}
		}
	};
</script>
