<template>
  <div class="login-container">
    <el-form ref="loginForm"
             :model="loginForm"
             :rules="loginRules"
             class="login-form"
             auto-complete="on"
             label-position="left">
      <div class="title-container">
        <h3 class="title">亿商支付管理系统</h3>
      </div>

      <el-form-item prop="UserName">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input ref="UserName"
                  v-model="loginForm.userName"
                  placeholder="请输入用户名"
                  name="userName"
                  type="text"
                  tabindex="1"
                  auto-complete="on" />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input :key="passwordType"
                  ref="password"
                  v-model="loginForm.password"
                  :type="passwordType"
                  placeholder="请输入密码"
                  name="password"
                  tabindex="2"
                  auto-complete="on"
                  @keyup.enter.native="handleLogin" />
        <span class="show-pwd"
              @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

	   <el-row>
	       <el-col :lg="16">
	        <el-form-item prop="code">
				<span class="svg-container">
				  <svg-icon icon-class="guide" />
				</span>
	       <el-input ref="code"
	           v-model="loginForm.code"
	           placeholder="请输入验证码"
	           name="code"
	           type="text"
	           tabindex="1"
	           auto-complete="on" />
	        </el-form-item>
	       </el-col>
	       <el-col :lg="8">
	         <div class="demo-image__preview" style="padding-left: 10px;padding-top: 4px;">
	           <img  @click="getcode()" style="width: 138px;height: 45px;" v-bind:src="img" ></img>
	         </div>
	       </el-col>
	   </el-row>

      <el-button :loading="loading"
                 type="primary"
                 style="width:100%;margin-bottom:30px;"
                 @click.native.prevent="handleLogin">登陆</el-button>
    </el-form>
  </div>
</template>

<script>
// import { constantRoutes1 } from '../../router'
import Layout from "@/layout";
export default {
  name: "Login",
  data () {
    const validateUsername = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入用户名"));
      } else {
        callback();
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error("密码长度最低6位"));
      } else {
        callback();
      }
    };
	const validateCode = (rule, value, callback) => {
	  if (!value) {
	    callback(new Error("请输入验证码"));
	  } else {
	    callback();
	  }
	};
    return {
	  img: process.env.VUE_APP_BASE_API+ '/pay/user/getcode',
      loginForm: {
        userName: "",
        password: "",
		code:""
      },
      loginRules: {
        userName: [
          { required: true, trigger: "blur", validator: validateUsername }
        ],
        password: [
          { required: true, trigger: "blur", validator: validatePassword }
        ],
		code: [
		  { required: true, trigger: "blur", validator: validateCode }
		]
      },
      loading: false,
      passwordType: "password",
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  methods: {

	  getcode(){
		  this.img=process.env.VUE_APP_BASE_API+"/pay/user/getcode?"+Math.random();
	  },

    showPwd () {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    },
    handleLogin () {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          const list = [
            {
              path: "/dashboard",
              component: Layout,
              children: [
                {
                  path: "dashboard",
                  name: "dashboard",
                  component: () => import("@/views/dashboard/index"),
                  meta: { title: "Form2", icon: "form" }
                }
              ]
            }
          ];


          this.$store
            .dispatch("user/login", this.loginForm)
            .then(() => { 
              this.$router.push({ 
				  path: "/",
			  });
			  this.$router.go(0);
              this.loading = false;
            })
            .catch(() => {
              this.loading = false;
            });

		  this.loginForm.code=""

        } else {
          console.log("error submit!!");
          return false;
        }
      });
    }
  }
};
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
