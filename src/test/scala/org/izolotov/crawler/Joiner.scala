//package org.izolotov.crawler
//
//import org.apache.spark.sql.types.IntegerType
//import org.apache.spark.sql.{SaveMode, SparkSession}
//import org.scalatest.FlatSpec
//
//class Joiner extends FlatSpec{
//
//  it should "..." in {
//    val spark = SparkSession
//      .builder
//      .master("local[*]") //This is the key config change to make it a local in process spark session.
//      .appName("myApp")
//      .getOrCreate()
//    import spark.implicits._
//    val pids = Seq(350, 154, 134, 411, 310)
//    pids.map{
//      pid =>
//        val sf = spark.read.option("header", "true").option("encoding", "utf-8").option("delimiter",",")
//          .csv(s"/home/izolotov/Documents/DebtPeriods/PIDMembersTotals/SF_Members_${pid}_2021_03.csv")
//        val qs = spark.read.option("header", "true").option("encoding", "utf-8").option("delimiter","\t")
//          .csv(s"/home/izolotov/Documents/DebtPeriods/PIDMembersTotals/RS_Members_${pid}_2021_03.csv")
//        sf.createOrReplaceGlobalTempView("sf")
//        qs.createOrReplaceGlobalTempView("qs")
//        spark.sql(
//          s"""
//             |select
//             |  coalesce(MemberId, member_id) as member_id,
//             |  sf.`P1Balance of Accounts` as sf_balance_of_accounts,
//             |  sf.`P1No. of Accounts` as sf_no_of_accounts,
//             |  sf.`P1Own Cu Balance of Accounts` as sf_own_balance_of_accounts,
//             |  sf.`P1Own Cu No. of Accounts` as sf_own_no_of_accounts,
//             |  '' as delimiter,
//             |  qs.`balance_of_accounts` as qs_balance_of_accounts,
//             |  qs.`no_of_accounts` as qs_no_of_accounts,
//             |  qs.`own_balance_of_accounts` as qs_own_balance_of_accounts,
//             |  qs.`own_no_of_accounts` as qs_own_no_of_accounts
//             |from
//             |  global_temp.sf sf
//             |full outer join global_temp.qs qs on sf.MemberID = qs.member_id
//             |""".stripMargin)
//          .repartition(1)
//          .na.fill(0)
//          .withColumn("sf_balance_of_accounts", $"sf_balance_of_accounts".cast(IntegerType))
//          .withColumn("sf_own_balance_of_accounts", $"sf_own_balance_of_accounts".cast(IntegerType))
//          .write.mode(SaveMode.Overwrite).option("header", "true")
//          .csv(s"/home/izolotov/Documents/DebtPeriods/PIDMembersTotals/Members_${pid}_2021_03")
//    }
//  }
//
//}
