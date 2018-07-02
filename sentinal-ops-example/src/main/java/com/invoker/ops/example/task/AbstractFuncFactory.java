package com.invoker.ops.example.task;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author fangyuan.lw
 * @date 2018/02/27
 */
public abstract class AbstractFuncFactory implements BizFuncFactory {

    @Override
    public BizFunc create(BizFunc next) {
        return new BizFunc() {
            @Override
            public BaseResponse apply(FuncName apiName, BaseRequest req, Object[] args) {
                return AbstractFuncFactory.this.apply(next, apiName, req, args);
            }
        };
    }

    protected abstract BaseResponse apply(BizFunc next, FuncName funcName, BaseRequest req, Object[] args);

    static class CountTask extends RecursiveTask<Integer> {

        private final Integer thre = 2;

        private int start;

        private int end;

        public CountTask(int start, int end) {
            this.start = start;
            this.end = end;
        }


        @Override
        protected Integer compute() {

            int sum = 0;
            boolean canCompute = (this.end - this.start) <= thre;
            if (canCompute) {
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
            } else {
                int middle = (this.start + this.end) / 2;
                CountTask leftTask = new CountTask(this.start, middle);
                CountTask rightTask = new CountTask(middle + 1, this.end);
                leftTask.fork();
                rightTask.fork();

                Integer leftResult = leftTask.join();
                Integer rightResult = rightTask.join();
                sum = leftResult + rightResult;
            }

            return null;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CountTask task = new CountTask(1, 4);
        Future<Integer> resultFu = forkJoinPool.submit(task);
        System.out.println(resultFu.get());


        Map<String, String> examp = Maps.newHashMap(ImmutableMap.of("key1", "value1", "key2", "value2"));

        StringBuffer sbf = new StringBuffer();
        examp.forEach((k, v) -> {
            sbf.append(k).append(":").append(v).append(";");
        });
        System.out.println(sbf.toString().substring(0, sbf.length() - 1));

        PriceInfo p1 = new PriceInfo();
        p1.setCarType(2L);
        p1.setPrice("23");
        PriceInfo p2 = new PriceInfo();
        p2.setCarType(3L);
        p2.setPrice("20");
        PriceInfo p3 = new PriceInfo();
        p3.setCarType(2L);
        p3.setPrice("19");
        PriceInfo p4 = new PriceInfo();
        p4.setCarType(2L);
        p4.setPrice("30");
        List<PriceInfo> list = Lists.newArrayList(ImmutableList.of(p1, p2, p3, p4));
        Comparator<String> vr = (s1, s2) -> BigDecimal.valueOf(Double.valueOf(s1)).compareTo(BigDecimal.valueOf(Double.valueOf(s2)));
        TreeMultimap<Long, String> keyMap = TreeMultimap.create(Ordering.natural(), Ordering.from(vr));
        list.stream().forEach(priceInfo -> {
            keyMap.put(priceInfo.getCarType(), priceInfo.getPrice());
        });
        List<PriceInfo> result = list.stream().filter(priceInfo -> {
            Long carType = priceInfo.getCarType();
            if (null != keyMap.get(carType) && keyMap.get(carType).size() > 1) {
                return keyMap.get(carType).first().equals(priceInfo.getPrice());
            }
            return true;
        }).collect(Collectors.toList());
        System.out.println(1);


        List<String> tlist = Lists.newArrayList(ImmutableList.of("welcome", "to", "guava", "java", "lambda"));
        System.out.println("排序前：" + JSON.toJSONString(tlist));
        //排序前：["welcome","to","guava","java","lambda"]
        tlist = Ordering.usingToString().sortedCopy(tlist);
        System.out.println("排序后" + JSON.toJSONString(tlist));
        //排序后["guava","java","lambda","to","welcome"]

        Function<PriceInfo, Long> sortCarTypeFunction = new Function<PriceInfo, Long>() {
            @Override
            public Long apply(PriceInfo priceInfo) {
                return priceInfo.getCarType();
            }
        };
        Ordering<PriceInfo> carTypeOrdering = Ordering.natural().reverse().nullsFirst().onResultOf(sortCarTypeFunction);
        list = carTypeOrdering.sortedCopy(list);
        System.out.println();


        List<String> filterList = Lists.newArrayList("welcome", "to", "guava", "java", "lambda");
        Predicate<String> lessThenPredicate = new Predicate<String>() {
            @Override
            public boolean apply(String s) {
                return s.length() > 5;
            }
        };
        Iterable<String> filterResult = Iterables.filter(filterList, Predicates.or((Predicates.or(Predicates.equalTo("guava"), Predicates.equalTo("java"))), lessThenPredicate));
        Preconditions.checkArgument(Lists.newArrayList(filterResult).containsAll(Lists.newArrayList("guava", "java", "to")), "集合包含：[guava,welcome,lambda,java]");
    }
}
