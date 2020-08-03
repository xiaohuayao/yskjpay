<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="用户名">
        <el-input v-model="form.username" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="角色">
        <el-input v-model="form.rolename" :disabled="true"></el-input>
      </el-form-item>

      <el-form-item label="用户状态">
        <el-radio-group v-model="form.state" :disabled="true">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">停用</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="所属行社" v-if="isroot === 2">
        <el-input v-model="form.clubname" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="所属网点" v-if="isroot === 3">
        <el-input v-model="form.branchname" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="所属商户" v-if="isroot === 4">
        <el-input v-model="form.merchantname" :disabled="true"></el-input>
      </el-form-item>

      <el-form-item label="创建人">
        <el-input v-model="form.createuser" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-input v-model="form.createtime" :disabled="true"></el-input>
      </el-form-item>
      <el-from-item>
        <div align="center">
          <el-button
            type="primary"
            v-waves
            @click="editData()"
            icon="el-icon-edit"
            style="width: 100%;"
          >修改密码</el-button>
        </div>
      </el-from-item>
    </el-form>

    <el-dialog id="editForm" title="修改密码" :center="true" :visible.sync="editForm.show">
      <el-form
        :rules="rules"
        status-icon
        :model="editForm"
        ref="editForm"
        style="max-width: 880px;margin: 0 auto;"
        label-position="right"
        label-width="80px"
      >
        <el-row :gutter="100">
          <el-col :lg="24">
            <span class="svg-container">
              <svg-icon icon-class="oldPwd" />
            </span>
            <el-form-item label="旧密码" prop="oldPwd">
              <el-input v-model="editForm.oldPwd" placeholder="请输入旧密码" type="password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="100">
          <el-col :lg="24">
            <el-form-item label="新密码" prop="newPwd">
              <el-input v-model="editForm.newPwd" placeholder="请输入新密码" type="password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="100">
          <el-col :lg="24">
            <el-form-item label="请确认" prop="newPwds">
              <el-input v-model="editForm.newPwds" placeholder="请再次输入新密码" type="password"></el-input>
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
import { getInfo, edit } from "@/api/user.js";

export default {
  data() {
    var checkPass = (rule, value, callback) => {
      if (this.editForm.newPwd == "") {
        return callback(new Error("请输入密码"));
      }
      if (this.editForm.newPwd !== this.editForm.newPwds) {
        return callback(new Error("两次输入的密码不相同"));
      } else {
        callback();
      }
    };
    let patter = /^(?![0-9]+$)(?![a-zA-Z]+$)[^]{6,16}$/;
    var checkPassword = (rule, value, callback) => {
      if (this.editForm.newPwd == "") {
        checkPass(rule, value, callback);
        return callback(new Error("请输入密码"));
      }
      if (!patter.test(value)) {
        return callback(new Error("至少包含字母和数字且大于5位小于17位"));
      } else {
        callback();
      }
    };
    return {
      isroot: "",
      form: {
        username: "",
        rolename: "",
        state: "",
        createuser: "",
        createtime: "",
        clubname: "",
        branchname: "",
        merchantname: "",
      },
      editForm: {
        show: false,
        oldPwd: "",
        newPwd: "",
        newPwds: "",
      },
      rules: {
        oldPwd: [
          {
            required: true,
            message: "请输入旧密码",
            trigger: "blur",
          },
        ],
        newPwd: [
          {
            validator: checkPassword,
            trigger: "blur",
          },
        ],
        newPwds: [
          {
            validator: checkPass,
            trigger: "blur",
          },
        ],
      },
    };
  },

  created() {
    this.fetchData();
  },

  methods: {
    fetchData() {
      getInfo().then((response) => {
        //debugger;
        (this.form.username = response.user.username),
          (this.form.rolename = response.user.rolename),
          (this.form.state = response.user.state),
          (this.form.createuser = response.user.createuser),
          (this.form.createtime = response.user.createtime),
          (this.form.clubname = response.user.clubname),
          (this.form.branchname = response.user.branchname),
          (this.form.merchantname = response.user.merchantname),
          (this.isroot = response.user.isroot);
      });
    },
    editData() {
      this.editForm.show = true;
      this.editForm.oldPwd = "";
      this.editForm.newPwd = "";
      this.editForm.newPwds = "";
      this.$nextTick(() => {
        this.$refs.editForm.clearValidate();
        this.$refs.editForm.resetFields();
      });
    },
    //提交表单
    onSubmit(editForm) {
      this.$refs[editForm].validate((valid) => {
        if (valid) {
          edit({
            oldpwd: this.editForm.oldPwd,
            newpwd: this.editForm.newPwd,
            newpwds: this.editForm.newPwds,
          }).then((res) => {
            this.$notify({
              title: "提示",
              message: "修改密码成功！",
              type: "success",
            });
            this.fetchData();
            this.editForm.show = false;
          });
        } else {
          return false;
        }
      });
    },
    //取消
    cancel() {
      this.editForm.show = false;
    },
  },
};
</script>