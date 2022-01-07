<template>
  <div class="homeBox">
    <!-- 查询 -->
    <div class="searchBox">
      <el-button
        type="primary"
        style="position: absolute; right: 0"
        @click="orderDrawer = true"
        >新增</el-button
      >
    </div>
    <!-- 列表 -->
    <el-table :data="dataList" stripe border style="width: 100%">
      <el-table-column prop="instanceTitle" label="实例编号"></el-table-column>
      <el-table-column prop="serialNum" label="编号"></el-table-column>
      <el-table-column prop="selectUser" label="选择用户"></el-table-column>
      <el-table-column prop="clientOrder" label="客户订单号"></el-table-column>
      <el-table-column prop="handInDate" label="交货日期"></el-table-column>
      <el-table-column prop="note" label="备注"></el-table-column>
      <el-table-column prop="clientEntire" label="客户全称"></el-table-column>
      <el-table-column prop="prepaidOrder" label="预付订单"></el-table-column>
      <el-table-column prop="deposit" label="订金（元）"></el-table-column>
      <el-table-column
        prop="depositMoney"
        label="订单金额（元）"
      ></el-table-column>

      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-button
            style="color: #2e63c8"
            type="text"
            @click="deleteRowData(scope.row.instanceTitle)"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="parameter.pageNum"
      :page-sizes="[5, 10, 15, 20]"
      :page-size="parameter.pageSize"
      :total="listTotal"
      background
      layout="total, sizes, prev, pager, next, jumper"
      style="float: right; margin-top: 30px"
    >
    </el-pagination>
    <!-- 新增 -->
    <el-drawer
      title="新增订单"
      :visible.sync="orderDrawer"
      direction="rtl"
      size="60%"
      @close="closeDrawer"
    >
      <el-form ref="form" :model="orderForm" label-width="100px">
        <el-form-item label="编号">
          <el-input v-model="orderForm.serialNum"></el-input>
        </el-form-item>
        <el-form-item label="选择客户">
          <!-- <el-input v-model="orderForm.selectUser"></el-input> -->
          <el-select v-model="orderForm.selectUser" placeholder="请选择" clearable>
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客户订单号">
          <el-input v-model="orderForm.clientOrder"></el-input>
        </el-form-item>
        <el-form-item label="交货日期">
          <el-date-picker
            v-model="orderForm.handInDate"
            type="date"
            placeholder="选择日期"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="orderForm.note"></el-input>
        </el-form-item>
        <el-form-item label="预付订单">
          <el-radio-group v-model="orderForm.prepaidOrder">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="订金">
          <el-input v-model="orderForm.deposit" type="number"></el-input>
        </el-form-item>
        <el-form-item label="订金总额">
          <el-input v-model="orderForm.depositMoney" type="number"></el-input>
        </el-form-item>
        <el-form-item label="商品明细" style="width: 100%">
          <el-table
            ref="multipleTable"
            :data="goodList"
            border
            max-height="500"
            style="width: 100%"
          >
            <el-table-column type="type" label="序号" width="50">
              <template slot-scope="scope">
                {{ scope.$index + 1 }}
              </template>
            </el-table-column>
            <el-table-column prop="aa" label="选择商品">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.aa"
                  placeholder="请选择"
                  @focus="openDialog(scope.$index)"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="a" label="商品名称"> </el-table-column>
            <el-table-column prop="b" label="商品分类"> </el-table-column>
            <el-table-column prop="c" label="商品编号"> </el-table-column>
            <el-table-column prop="d" label="规格"> </el-table-column>
            <el-table-column prop="e" label="单位"> </el-table-column>
            <el-table-column prop="f" label="单价/元">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.f"
                  placeholder="请输入数字"
                  type="number"
                  @input="allMoney(scope.$index, scope.row)"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="g" label="*数量">
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.g"
                  placeholder="请输入数字"
                  type="number"
                  @input="allMoney(scope.$index, scope.row)"
                ></el-input>
              </template>
            </el-table-column>
            <el-table-column prop="h" label="合计"> </el-table-column>
            <el-table-column label="操作" width="60">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  size="small"
                  @click="deleteData(scope.$index)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <el-button style="margin-top: 15px" @click="addGoodsData"
            ><i class="el-icon-plus"></i>新增一项</el-button
          >
        </el-form-item>
        <el-form-item
          style="
            display: flex;
            align-items: center;
            justify-content: center;
            width: 100%;
            margin-top: 30px;
          "
        >
          <el-button type="primary" @click="addOrderClick">确认</el-button>
          <el-button @click="orderDrawer = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
    <!-- dialog商品 -->
    <el-dialog title="商品" :visible.sync="dialogTable" center width="50%">
      <el-table
        :data="dialogGoodsData"
        stripe
        border
        style="width: 100%; cursor: pointer"
        @row-click="rowClick"
      >
        <el-table-column prop="a" label="商品名称"></el-table-column>
        <el-table-column prop="b" label="商品分类"></el-table-column>
        <el-table-column prop="c" label="商品编号"></el-table-column>
        <el-table-column prop="d" label="规格"></el-table-column>
        <el-table-column prop="e" label="单位"></el-table-column>
        <el-table-column prop="f" label="单价/元"></el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
// import axios from "axios";
import {
  getDingTalkUserInfo,
  getOrderList,
  addOrder,
  deleteOrder,
  getCorpId
} from "../api";
import { getCode } from "../utils/dindin";
import { Message } from "element-ui";
export default {
  props: {},
  components: {},
  data() {
    return {
      parameter: {
        pageNum: 1,
        pageSize: 10,
      },
      listTotal: 5,
      dataList: [
        {
          instanceTitle: "标题",
          serialNum: "001",
          selectUser: "选择用户",
          clientOrder: "20211218",
          handInDate: "2021-12-24",
          note: "请尽快发货",
          clientSerialNum: "C001",
          clientEntire: "路人甲的全称",
          clientName: "路人甲",
          clientPhone: "15896358753",
          clientAddress: "江苏省南京市",
          prepaidOrder: "20211218",
          deposit: "1000",
          depositMoney: "1000",
          createTime: "2021-12-18",
          updataTime: "2121-12-18",
        }
      ],
      // 新增
      orderDrawer: false,
      options: [
        {
          value: "李先生",
          label: "李先生",
        },
        {
          value: "张小姐",
          label: "张小姐",
        },
        {
          value: "王先生",
          label: "王先生",
        },
        {
          value: "刘小姐",
          label: "刘小姐",
        },
      ],
      orderForm: {
        serialNum: "", // 编号
        selectUser: "", // 选择客户
        clientOrder: "", // 客户订单号
        handInDate: "", // 交货日起
        note: "", // 备注
        prepaidOrder: "0", // 是否预付订单
        deposit: "", // 订金
        depositMoney: "", // 订金总额
      },
      selectInputIndex: "",
      baseData: {
        id: "",
        aa: "", // 选择商品
        a: "--", // 商品名称
        b: "--", // 商品分类
        c: "--", // 商品编号
        d: "--", // 规格
        e: "--", // 单位
        f: "", // 单价
        g: "", // 数量
        h: "--", // 合计
      },
      goodList: [],
      // dialog
      dialogTable: false,
      dialogGoodsData: [
        {
          id: "001",
          a: "商品A",
          b: "1类",
          c: "KJLJDSL6565",
          d: "11mm",
          e: "箱",
          f: 199,
        },
        {
          id: "002",
          a: "商品B",
          b: "2类",
          c: "HJHBK352153",
          d: "11cm",
          e: "件",
          f: 299,
        },
        {
          id: "003",
          a: "商品C",
          b: "3类",
          c: "KLASKL744",
          d: "10mm",
          e: "箱",
          f: 99,
        },
        {
          id: "004",
          a: "商品D",
          b: "1类",
          c: "SIU5445",
          d: "11m",
          e: "件",
          f: 399,
        },
      ],
    };
  },
  mounted() {
    if (this.goodList.length === 0) {
      this.addGoodsData();
    }
    getCorpId({ code: ""}).then((resp) => {
      if (resp != null && resp.status == 200) {
        let corpId = resp.data.data;
        Message.info("corpId:" + corpId);
        // 获取钉钉免登code、参数：回调函数
        getCode((code) => {
          Message.info("code:" + code);
          if (code === '' ) {
            Message.error("获取用户免登授权码失败!");
            return;
          }
          getDingTalkUserInfo({ code: code }).then((res) => {
            if (res != null && res.status == 200) {
              let data = res.data.data;
              Message.info("用户id：" + data.userId);
              localStorage.setItem("dduserid", data.userId);
              getOrderList({"userId": data.userId}).then((res) => {
                this.dataList = res.data.data;
              });
            }
          });
        }, corpId);
      }
    })
  },
  methods: {

    addOrderClick() {
      let userId = localStorage.getItem("dduserid");
      this.orderForm.goodlist = this.goodList;
      this.orderForm.userId = userId;
      console.log(userId);
      let data = {...this.orderForm}
      data.handInDate = new Date(data.handInDate).getTime()
      addOrder(data).then((res) => {
        // Message.info(res);
        if (res.data.code == 200) {
          this.$message.success('新增成功')
        }
        getOrderList({"userId": userId}).then((res) => {
          this.dataList = res.data.data;
        });
        this.orderDrawer = false;
        this.goodList = [];
        this.addGoodsData()
        this.$refs.form.resetFields();
      });
    },
    closeDrawer() {
      this.orderForm = {
        serialNum: "", // 编号
        selectUser: "", // 选择客户
        clientOrder: "", // 客户订单号
        handInDate: "", // 交货日起
        note: "", // 备注
        prepaidOrder: "0", // 是否预付订单
        deposit: "", // 订金
        depositMoney: "", // 订金总额
      }
    },
    // size
    handleSizeChange(val) {
      Message.info(`每页 ${val} 条`);
      this.parameter.pageNum = val;
      this.getOrderList();
    },
    // 页码
    handleCurrentChange(val) {
      Message.info(`当前页: ${val}`);
      this.parameter.pageSize = val;
      this.getOrderList();
    },
    // 新增一项
    addGoodsData() {
      this.goodList.push(JSON.parse(JSON.stringify(this.baseData)));
    },
    // 打开弹窗选择商品
    openDialog(i) {
      this.selectInputIndex = i;
      this.dialogTable = true;
    },
    // 选择商品
    rowClick(row) {
      this.goodList[this.selectInputIndex].aa = row.a;
      this.goodList[this.selectInputIndex].h = 0;
      Object.assign(this.goodList[this.selectInputIndex], row);
      this.dialogTable = false;
    },
    // 合计价格
    allMoney(i, scope) {
      if (scope?.f && scope?.g) {
        this.goodList[i].h = scope.f * scope.g;
      } else {
        this.goodList[i].h = 0;
      }
    },
    // 删除
    deleteData(i) {
      this.$confirm("此操作将永久删除当前数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.goodList.splice(i, 1);
          this.$message({
            type: "success",
            message: "删除成功!",
          });

        })
        .catch(() => {});
    },

    deleteRowData(id) {
      console.log(id);
      deleteOrder({
        'userId': localStorage.getItem("dduserid"),
        'id': id}).then(res => {
        if (res.data.code == 200) {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          getOrderList({"userId": localStorage.getItem("dduserid")}).then((res) => {
            this.dataList = res.data.data;
          });
        }
      })
    }
  },
};
</script>

<style scoped lang="scss">
::v-deep {
  .el-form {
    display: flex;
    flex-wrap: wrap;
    padding: 0 30px;
    .el-form-item {
      width: 45%;

      .el-date-editor.el-input {
        width: 100%;
      }
      .el-select {
        width: 100%;
      }
    }
  }
  input::-webkit-outer-spin-button,
  input::-webkit-inner-spin-button {
    -webkit-appearance: none !important;
    margin: 0;
  }
  input[type="number"] {
    -moz-appearance: textfield;
  }
}
.homeBox {
  margin: 0;
  padding: 0 20px;

  .searchBox {
    position: relative;
    display: flex;
    align-items: center;
    // justify-content: space-between;
    padding: 50px 0 50px 0;
    width: 100%;
    & > .el-input {
      width: 20%;
      margin-right: 5%;
    }
  }
}
</style>
