<template>
	<div class="app-container">
		<!--列表开始-->
		<div class="filter-container">
			<el-input v-model="query.search" style="width: 13%;" :placeholder="search" />
			<el-date-picker v-model="time" type="datetimerange"  :picker-options="pickerOptions"
			 range-separator="至" start-placeholder="请选择开始日期" end-placeholder="请选择结束日期" align="right" :default-time="['00:00:00', '23:59:59']">
			</el-date-picker>

			<el-button type="primary" icon="el-icon-search" @click="fetchData(1)">查询</el-button>
			<el-button type="danger" icon="el-icon-search" @click="handleReset">重置</el-button>
		</div>


		<el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
			<el-table-column label="序号" fixed="left" type="index" :index="indexMethod" align="center" width="65"></el-table-column>
			<el-table-column :label="name" align="center">
				<template slot-scope="scope">
					<div>{{ scope.row.merchname }}</div>
				</template>
			</el-table-column>

			<el-table-column label="威富通账号" align="center">
				<el-table-column label="统一收单账号" align="center" :show-overflow-tooltip="true">
					<template slot-scope="scope">
						<div>{{ scope.row.swiffpassmerchno }}</div>
						<hr style="height:1px;border:none;border-top:1px solid #E4E4E4;width: 140%;margin-left: -10%;" />
						<div>{{ scope.row.merid }}</div>
					</template>
				</el-table-column>
			</el-table-column>

			<el-table-column label="笔数" align="center">
				<template slot-scope="scope">
					<div v-if="scope.row.swiffCount === null">0</div>
					<div>{{ scope.row.swiffCount }}</div>
					<hr style="height:1px;border:none;border-top:1px solid #E4E4E4;width: 140%;margin-left: -10%;" />
					<div v-if="scope.row.merCount === null">0</div>
					<div>{{ scope.row.merCount }}</div>
				</template>
			</el-table-column>

			<el-table-column label="金额" align="center">
				<template slot-scope="scope">
					<div v-if="scope.row.swiffMoney === null">0</div>
					<div>{{ scope.row.swiffMoney }}</div>
					<hr style="height:1px;border:none;border-top:1px solid #E4E4E4;width: 140%;margin-left: -10%;" />
					<div v-if="scope.row.merMoney === null">0</div>
					<div>{{ scope.row.merMoney }}</div>
				</template>
			</el-table-column>

			<el-table-column label="退款笔数" align="center">
				<template slot-scope="scope">
					<div v-if="scope.row.swiffRefundCount === null">0</div>
					<div>{{ scope.row.swiffRefundCount }}</div>
					<hr style="height:1px;border:none;border-top:1px solid #E4E4E4;width: 140%;margin-left: -10%;" />
					<div v-if="scope.row.merRefundCount === null">0</div>
					<div>{{ scope.row.merRefundCount }}</div>
				</template>
			</el-table-column>

			<el-table-column label="退款金额" align="center">
				<template slot-scope="scope">
					<div v-if="scope.row.swiffRefundMoney === null">0</div>
					<div>{{ scope.row.swiffRefundMoney }}</div>
					<hr style="height:1px;border:none;border-top:1px solid #E4E4E4;width: 140%;margin-left: -10%;" />
					<div v-if="scope.row.merRefundMoney === null">0</div>
					<div>{{ scope.row.merRefundMoney }}</div>
				</template>
			</el-table-column>

			<el-table-column label="交易净额" align="center">
				<template slot-scope="scope">
					<div v-if="scope.row.swiffMoneyTrue === null">0</div>
					<div>{{ scope.row.swiffMoneyTrue }}</div>
					<hr style="height:1px;border:none;border-top:1px solid #E4E4E4;width: 140%;margin-left: -10%;" />
					<div v-if="scope.row.merMoneyTrue === null">0</div>
					<div>{{ scope.row.merMoneyTrue }}</div>
				</template>
			</el-table-column>

			<el-table-column label="总笔数" align="center">
				<template slot-scope="scope">
					<span v-if="scope.row.orderCount === null">0</span>
					{{ scope.row.orderCount }}
				</template>
			</el-table-column>
			<el-table-column label="总金额" align="center">
				<template slot-scope="scope">
					<span v-if="scope.row.money === null">0</span>
					{{ scope.row.money }}
				</template>
			</el-table-column>

			<el-table-column label="总退款笔数" align="center">
				<template slot-scope="scope">
					<span v-if="scope.row.refundCount === null">0</span>
					{{ scope.row.refundCount }}
				</template>
			</el-table-column>

			<el-table-column label="总退款金额" align="center">
				<template slot-scope="scope">
					<span v-if="scope.row.refundMoney === null">0</span>
					{{ scope.row.refundMoney }}
				</template>
			</el-table-column>

			<el-table-column label="交易总净额" align="center">
				<template slot-scope="scope">
					<span v-if="scope.row.moneyTrue === null">0</span>
					{{ scope.row.moneyTrue }}
				</template>
			</el-table-column>

		</el-table>

		<el-pagination :total="total" :current-page="page" :page-sizes="[ 15,20, 50, 100, 200, 300, 400]" style="margin-top: 8px;"
		 layout="total, prev, pager, next, sizes" @size-change="sizeChange" @current-change="pageChange" />
	</div>
</template>

<script>
	import {
		getpageListMerchPlus,
		getTimes
	} from "@/api/collect/collect.js";

	export default {
		filters: {
			statusFilter(status) {
				const statusMap = {
					published: "success",
					draft: "gray",
					deleted: "danger",
				};
				return statusMap[status];
			},
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
				isroot: "",
				name: "商户名称",
				search: "输入商户名称或支付通道账号",
				query: {
					branchname: "",
					merchantname: "",
					clubname: "",
					type: "1",
					startTime: "",
					endTime: "",
					page: 1,
					limit: 15,
				},
				formData: {
					DotName: "",
					DotAddress: "",
				},
			};
		},
		created() {
			this.fetchData();
		},

		methods: {
			fetchData(type) {
				if (type == 1) {
					this.query.page = 1;
				}
				this.listLoading = true;

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
				getpageListMerchPlus(this.query).then((response) => {
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
				this.time = [];
				this.pageChange("1");
				this.query.search = "";
				this.query.startTime = "";
				this.query.endTime = "";
				this.fetchData();
			},
			//获取时间
			fetchTime(type) {
				this.query.startTime = "";
				this.query.endTime = "";
				getTimes({
					type: type
				}).then((res) => {
					this.query.startTime = new Date(res.times.startTime);
					this.query.endTime = new Date(res.times.endTime);
				});
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
		padding: 50;

	}

</style>
