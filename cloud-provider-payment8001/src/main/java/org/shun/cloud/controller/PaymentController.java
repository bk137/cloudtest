package org.shun.cloud.controller;

import org.shun.cloud.entities.CommonResult;
import org.shun.cloud.entities.Payment;
import org.shun.cloud.service.PaymentService;
import org.shun.cloud.util.ShunPrint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.ws.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient;



    @PostMapping("/payment/create")
    public CommonResult paysey(@RequestBody Payment payment) {
        ShunPrint.print(payment);
        if (payment.getSerial() != null) {
            int res = paymentService.create(payment);
            log.info("PaymentController=>create，插入成功,serverPort:" + serverPort + res);
            if (res > 0) {
                return new CommonResult(200, "操作成功");
            }
        }
        return new CommonResult(520, "妹子你很可愛");
    }

    @GetMapping(value = "/payment/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info("PaymentController=>getById,数据是" + payment);
        if (payment != null) {
            return new CommonResult(200, "操作成功,serverPort:" + serverPort, payment);
        }
        return new CommonResult(520, "单身吗妹子", null);
    }


    @GetMapping(value = "/test")
    public void sele() {
        System.out.println(paymentService.getPaymentById(1L));
    }

    @GetMapping(value = "/payment/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            log.info("****element:" + element);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getInstanceId() + "\t" + instance.getHost() + "\t" + instance.getPort() + "\t" + instance.getUri());
        }
        return this.discoveryClient;
    }

    @GetMapping("/payment/lb")
    public String getPaymentLB() {
        return serverPort;
    }

    @GetMapping("/payment/timeout")
    public String paymentFeignTimeout(){

        try {
//            枚举 SECONDS秒单位
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  serverPort;
    }


}
