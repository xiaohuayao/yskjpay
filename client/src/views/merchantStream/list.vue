<template>
	<div class="app-container">
		<!--列表开始-->
		<div class="filter-container">
			<el-input v-model="query.merchantName" style="width: 13%;" placeholder="请输入商户" />
			<el-input v-model="query.operauser" style="width: 13%;" placeholder="请输入操作员" />
			<el-input v-model="query.swiffpassmerchno" style="width: 13%;" placeholder="请输入威富通商户号或统一收单账号" />
			<el-select v-model="query.paytype" style="width: 13%;" placeholder="请选择支付方式">
				<el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>
			<el-select v-model="query.paychannel" style="width: 13%;" placeholder="请选择支付通道">
				<el-option v-for="item in paychannel" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>

			<el-date-picker style="width: 14%;" v-model="time" type="datetimerange" :picker-options="pickerOptions"
			 range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" align="right" :default-time="['00:00:00', '23:59:59']">
			</el-date-picker>
			<el-button type="primary" icon="el-icon-search" @click="fetchData(1)">
				查询
			</el-button>
			<el-button type="danger" icon="el-icon-search" @click="handleReset">
				重置
			</el-button>
			<el-link :href="exportUrl">
				<el-button type="primary" icon="el-icon-download" @click="exportExcel">
					导出
				</el-button>
			</el-link>

			<el-row style="padding: 10px;">
				<p><span style="margin-right: 50px;">交易总金额:{{moneyNum}}</span>
					<span style="margin-right: 100px;">交易总数:{{ total }}</span>
					<span style="margin-right: 100px;">已支付数:{{havePaid}}</span>
					<span style="margin-right: 100px;">未支付数:{{notPaid}}</span>
					<span style="margin-right: 100px;">部分退款数:{{portionNum}}</span>
					<span style="margin-right: 100px;">全额退款数:{{allNum}}</span></p>
			</el-row>
		</div>


		<el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row
		 @sort-change="changeSort">
			<el-table-column label="序号" fixed="left" type="index" :index="indexMethod" align="center" width="65"></el-table-column>

			<el-table-column label="所属商户" align="center">
				<template slot-scope="scope">
					{{ scope.row.merchantName }}
				</template>
			</el-table-column>
			<el-table-column label="付款金额" align="center">
				<template slot-scope="scope">
					{{ scope.row.money }}
				</template>
			</el-table-column>
			<el-table-column label="威富通商户号" align="center">
				<template slot-scope="scope">
					{{ scope.row.swiffpassmerchno }}
				</template>
			</el-table-column>
			<el-table-column label="操作员" align="center">
				<template slot-scope="scope">
					<span>{{ scope.row.operauser }}</span>
				</template>
			</el-table-column>
			<!-- <el-table-column label="支付状态" align="center">
	    <template slot-scope="scope">
			<span v-if="scope.row.paystate === 1">已支付</span>
			<span v-if="scope.row.paystate === 0">未支付</span>
	    </template>
	  </el-table-column> -->
			<el-table-column label="支付状态" align="center">
				<template slot-scope="scope">
					<span>{{ scope.row.paystateName }}</span>
				</template>
			</el-table-column>
			<el-table-column label="支付方式" align="center">
				<template slot-scope="scope">
					<span>{{ scope.row.paytype }}</span>
				</template>
			</el-table-column>
			<el-table-column label="支付通道" align="center">
				<template slot-scope="scope">
					<span v-if="scope.row.paychannel === 1">威富通</span>
					<span v-if="scope.row.paychannel === 2">统一收单</span>
				</template>
			</el-table-column>
			<el-table-column label="支付时间" align="center" prop="paydate" sortable="custom">
				<template slot-scope="scope">
					<div>{{handleDate(scope.row.paydate)}}</div>
					<div>{{handleTime(scope.row.paydate)}}</div>
				</template>
			</el-table-column>
			<el-table-column label="订单号" align="center">
				<el-table-column label="付款码" align="center" :show-overflow-tooltip="true" width="175px">
					<template slot-scope="scope">
						<div>{{ scope.row.orderno }}</div>
						<div>{{ scope.row.authcode }}</div>
					</template>
				</el-table-column>
			</el-table-column>
			<el-table-column label="退款订单号" align="center">
				<template slot-scope="scope">
					<span>{{ scope.row.refundorderno }}</span>
				</template>
			</el-table-column>
			<el-table-column label="创建时间" align="center" prop="createtime" sortable="custom">
				<template slot-scope="scope">
					<div>{{handleDate(scope.row.createtime)}}</div>
					<div>{{handleTime(scope.row.createtime)}}</div>
				</template>
			</el-table-column>

			<!-- <el-table-column label="操作" align="center" width="150px">
				<template slot-scope="scope">
					<el-button @click="editData(scope.row)" type="success" size="mini" icon="el-icon-plus" v-if="scope.row.paystate === 'HAVE'">
						退款
					</el-button>
				</template>
			</el-table-column> -->
		</el-table>

		<el-pagination :total="total" :current-page="query.page" :page-sizes="[15, 20, 50, 100, 200, 300, 400]" style="margin-top: 8px;"
		 layout="total, prev, pager, next, sizes" @size-change="sizeChange" @current-change="pageChange" />

		<!-- 编辑-->
		<el-dialog id="editForm" title="详情" :center="true" :visible.sync="editForm.show">
			<el-form :rules="editForm.rules" status-icon :model="editForm" ref="editForm" style="max-width: 880px;margin: 0 auto;"
			 label-position="right" label-width="100px">
				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="订单号:">
							<el-input v-model="editForm.orderno" placeholder="" :disabled="true"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row :gutter="100">
					<el-col :lg="24">
						<el-form-item label="退款金额:" prop="money">
							<el-input v-model="editForm.money" :placeholder="editForm.moneys" @keyup.native="editForm.money = oninput(editForm.money)">
							</el-input>
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
		edit
	} from "@/api/merchantStream/index";

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
				havePaid: 0,
				notPaid: 0,
				moneyNum: 0,
				portionNum: 0,
				allNum: 0,
				exportUrl: "",
				options: [{
						value: '微信',
						label: '微信'
					},
					{
						value: '银联',
						label: '银联'
					},
					{
						value: '支付宝',
						label: '支付宝'
					}
				],
				paychannel: [{
						value: 1,
						label: '威富通'
					},
					{
						value: 2,
						label: '统一收单'
					}
				],
				query: {
					startTime: "",
					endTime: "",
					paytype: "",
					merchantName: "",
					swiffpassmerchno: "",
					operauser: "",
					paychannel: "",
					page: 1,
					limit: 15,
					sorts: 2,
					sortRule: 2
				},
				formData: {
					DotName: "",
					DotAddress: ""
				},
				editForm: {
					show: false,
					id: "",
					orderno: "",
					money: "",
					moneys: "",
					rules: {
						money: [{
							required: true,
							message: "请输入退款金额",
							trigger: "blur"
						}]
					}
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
			fetchData(type) {
				this.listLoading = true;
				if (type == 1) {
					this.query.page = 1;
				}
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
					this.havePaid = response.num.havePaid == undefined ? 0 : parseInt(response.num.havePaid);
					this.notPaid = response.num.notPaid == undefined ? 0 : parseInt(response.num.notPaid);
					this.moneyNum = response.num.moneyNum;
					this.portionNum = response.num.portionNum == undefined ? 0 : parseInt(response.num.portionNum);
					this.allNum = response.num.allNum == undefined ? 0 : parseInt(response.num.allNum);
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
				this.time = [];
				this.query.startTime = "";
				this.query.endTime = "";
				this.query.paytype = "";
				this.query.merchantName = "";
				this.query.swiffpassmerchno = "";
				this.query.paychannel = "";
				this.query.operauser = "";
				this.query.sortRule = 2;
				this.query.sorts = 2;
				this.pageChange("1");
				this.fetchData();
			},
			//排序
			changeSort(val) {
				console.log(val)
				let sortRule = this.query.sortRule;
				if (val.prop == "createtime") {
					sortRule = 2;
				} else {
					sortRule = 1;
				}
				this.query.sortRule = sortRule;
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
				this.editForm.orderno = "";
				this.editForm.money = "";
				this.editForm.moneys = "";
				this.$nextTick(() => {
					this.$refs.editForm.clearValidate();
					this.$refs.editForm.resetFields();
				})
				this.editForm.id = row.id;
				this.editForm.orderno = row.orderno;
				this.editForm.moneys = row.money;
			},
			//提交表单
			onSubmit(editForm) {
				if (this.editForm.money > this.editForm.moneys) {
					this.$notify({
						title: '提示',
						message: '退款金额大于订单金额',
						type: 'error'
					});
					return;
				}
				this.$refs[editForm].validate((valid) => {
					if (valid) {
						edit({
							id: this.editForm.id,
							orderno: this.editForm.orderno,
							money: this.editForm.money
						}).then(res => {
							this.$notify({
								title: '提示',
								message: '退款操作成功!',
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
			},
			exportExcel() {
				this.exportUrl = process.env.VUE_APP_BASE_API + "/pay/order/exportExcelOrder?" + "&startTime=" + this.query.startTime +
					"&endTime=" + this.query.endTime + "&merchantName=" + this.query.merchantName + "&operauser=" + this.query.operauser +
					"&swiffpassmerchno=" + this.query.swiffpassmerchno + "&paytype=" + this.query.paytype + "&paychannel=" + this.query
					.paychannel
			}

		}
	};
</script>
