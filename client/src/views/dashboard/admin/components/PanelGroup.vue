<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="todayAmount" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            今日交易金额
          </div>
          <count-to :start-val="0" :end-val="todayDealMoney" :duration="2600" class="card-panel-num" :decimals='2 | decimalsFilter'/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('messages')">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon icon-class="todayCount" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            今日交易笔数
          </div>
          <count-to :start-val="0" :end-val="todayDealStock" :duration="3000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('purchases')">
        <div class="card-panel-icon-wrapper icon-money">
          <svg-icon icon-class="allAmount" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            累计交易金额
          </div>
          <count-to :start-val="0" :end-val="totalDealMoney" :duration="3200" class="card-panel-num" :decimals='2 | decimalsFilter'/>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('shoppings')">
        <div class="card-panel-icon-wrapper icon-shopping">
          <svg-icon icon-class="allCount" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            累计交易笔数
          </div>
          <count-to :start-val="0" :end-val="totalDealStock" :duration="3600" class="card-panel-num" />
        </div>
      </div>
    </el-col>
	<!--第二行 -->
	<el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
	  <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
	    <div class="card-panel-icon-wrapper icon-people">
	      <svg-icon icon-class="todayRefundAmount" class-name="card-panel-icon" />
	    </div>
	    <div class="card-panel-description">
	      <div class="card-panel-text">
	        今日退款金额
	      </div>
	      <count-to :start-val="0" :end-val="todayRefundMoney" :duration="2600" class="card-panel-num" :decimals='2 | decimalsFilter'/>
	    </div>
	  </div>
	</el-col>
	<el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
	  <div class="card-panel" @click="handleSetLineChartData('messages')">
	    <div class="card-panel-icon-wrapper icon-message">
	      <svg-icon icon-class="todayRefundCount" class-name="card-panel-icon" />
	    </div>
	    <div class="card-panel-description">
	      <div class="card-panel-text">
	        今日退款笔数
	      </div>
	      <count-to :start-val="0" :end-val="todayRefundStock" :duration="3000" class="card-panel-num" />
	    </div>
	  </div>
	</el-col>
	<el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
	  <div class="card-panel" @click="handleSetLineChartData('purchases')">
	    <div class="card-panel-icon-wrapper icon-money">
	      <svg-icon icon-class="allRefundAmount" class-name="card-panel-icon" />
	    </div>
	    <div class="card-panel-description">
	      <div class="card-panel-text">
	        累计退款金额
	      </div>
	      <count-to :start-val="0" :end-val="totalRefundMoney" :duration="3200" class="card-panel-num" :decimals='2 | decimalsFilter' />
	    </div>
	  </div>
	</el-col>
	<el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
	  <div class="card-panel" @click="handleSetLineChartData('shoppings')">
	    <div class="card-panel-icon-wrapper icon-shopping">
	      <svg-icon icon-class="allRefundCount" class-name="card-panel-icon" />
	    </div>
	    <div class="card-panel-description">
	      <div class="card-panel-text">
	        累计退款笔数
	      </div>
	      <count-to :start-val="0" :end-val="totalRefundStock" :duration="3600" class="card-panel-num" />
	    </div>
	  </div>
	</el-col>
	<!-- 第三行 -->
	<el-col :xs="12" :sm="12" :lg="6" class="card-panel-col" v-show="isroot === 1">
	  <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
	    <div class="card-panel-icon-wrapper icon-people">
	      <svg-icon icon-class="addMerchant" class-name="card-panel-icon" />
	    </div>
	    <div class="card-panel-description">
	      <div class="card-panel-text">
	        今日新增商户
	      </div>
	      <count-to :start-val="0" :end-val="todayMerchant" :duration="2600" class="card-panel-num" />
	    </div>
	  </div>
	</el-col>
	<el-col :xs="12" :sm="12" :lg="6" class="card-panel-col" v-show="isroot === 1">
	  <div class="card-panel" @click="handleSetLineChartData('messages')">
	    <div class="card-panel-icon-wrapper icon-message">
	      <svg-icon icon-class="merchant" class-name="card-panel-icon" />
	    </div>
	    <div class="card-panel-description">
	      <div class="card-panel-text">
	        累计商户总数
	      </div>
	      <count-to :start-val="0" :end-val="totalMerchant" :duration="3000" class="card-panel-num" />
	    </div>
	  </div>
	</el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
import {collect} from "@/api/dashboard/dashboard";

export default {
  components: {
    CountTo
  },
  data(){
	  return{
		 todayDealStock:"",
		 todayDealMoney:"",
		 todayRefundStock:"",
		 todayRefundMoney:"",
		 totalDealStock:"",
		 totalDealMoney:"",
		 totalRefundStock:"",
		 totalRefundMoney:"",
		 todayMerchant:"",
		 totalMerchant:"",
		 isroot:"",
		 type:"messages"
	  }
  },
  created() {
    this.handleSetLineChartData();
  },
  methods: {
    handleSetLineChartData(type) {
		collect().then(res => {
			this.todayDealStock = res.collect.todayDealStock;
			this.todayDealMoney = res.collect.todayDealMoney;
			this.todayRefundStock = res.collect.todayRefundStock;
			this.todayRefundMoney = res.collect.todayRefundMoney;
			this.totalDealStock = res.collect.totalDealStock;
			this.totalDealMoney = res.collect.totalDealMoney;
			this.totalRefundStock = res.collect.totalRefundStock;
			this.totalRefundMoney = res.collect.totalRefundMoney;
			this.totalMerchant = res.collect.totalMerchant;
			this.todayMerchant = res.collect.todayMerchant;
			this.isroot = res.collect.isroot;
		});
		this.$emit('handleSetLineChartData', type)
    },
	decimalsFilter(number){
	//这里判断这个输出的是几位小数位（自行处理）
	// if(判断number有几位小数){
	// return 2 //这里返回插件要显示的几位小数
	// }else{
	// return 0
	return number;
	}
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
