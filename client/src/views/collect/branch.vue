<template>
  <div class="app-container">
    <!--列表开始-->
    <div class="filter-container">
      <el-input v-model="query.search" style="width: 13%;" :placeholder="search" />
	  <el-date-picker
	  v-model="time"
	  type="datetimerange"
	  :picker-options="pickerOptions"
	  range-separator="至"
	  start-placeholder="请选择开始日期"
	  end-placeholder="请选择结束日期"
	  align="right"
	  :default-time="['00:00:00', '23:59:59']">
	  </el-date-picker>
	  
      <el-button type="primary" icon="el-icon-search" @click="fetchData(1)">
        查询
      </el-button>
      <el-button type="danger" icon="el-icon-search" @click="handleReset">
        重置
      </el-button>
    </div>
    <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row>
      <el-table-column label="序号" fixed="left" type="index" :index="indexMethod" align="center" width="65"></el-table-column>
    <el-table-column label="网点名称" align="center" v>
        <template slot-scope="scope">
          {{ scope.row.branchname }}
        </template>
      </el-table-column>
    <el-table-column label="交易笔数" align="center">
	    <template slot-scope="scope">
			<span v-if="scope.row.orderCount === null">0</span>
			<span>{{ scope.row.orderCount }}</span>
	    </template>
	  </el-table-column>
	  <el-table-column label="交易净额" align="center">
	    <template slot-scope="scope">
				<span v-if="scope.row.money === null">0</span>
				<span>{{ scope.row.money }}</span>
	    </template>
	  </el-table-column>

    </el-table>

    <el-pagination :total="total" :current-page="page" :page-sizes="[ 15,20, 50, 100, 200, 300, 400]" style="margin-top: 8px;"
      layout="total, prev, pager, next, sizes" @size-change="sizeChange" @current-change="pageChange" />


  </div>
</template>

<script>
  import {
  getpageListBranch,
  getTimes,
  } from "@/api/collect/collect.js";

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
        search:"请输入网点名称",
        query: {
          branchname:"",
          merchantname: "",
					clubname: "",
          type:"1",
		  startTime:"",
		  endTime:"",
          page: 1,
          limit: 15
        },
        formData: {
          DotName: "",
          DotAddress: ""
        },
        
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
		
		var arr = this.time;
		if(arr != undefined){
			this.query.startTime = arr[0];
			this.query.endTime = arr[1];
			if (this.query.startTime != undefined && this.query.startTime != "") {
				var s = new Date(this.query.startTime);
				this.query.startTime = s.getFullYear() + '-' + (s.getMonth() + 1) + '-' + s.getDate() + ' ' + s.getHours() + ':' +
					s.getMinutes() + ':' + s.getSeconds()
			}
			if (this.query.endTime != undefined && this.query.endTime != "") {
				var e = new Date(this.query.endTime);
				this.query.endTime = e.getFullYear() + '-' + (e.getMonth() + 1) + '-' + e.getDate() + ' ' + e.getHours() + ':' + e
					.getMinutes() + ':' + e.getSeconds()
			}
		}else{
			this.query.startTime = "";
			this.query.endTime = "";
		}
        
        getpageListBranch(this.query).then(response => {
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
		this.query.search= "";
    this.query.startTime = "";
	  this.query.endTime = "";
    this.fetchData();
      },

	  
    }
  };
</script>
