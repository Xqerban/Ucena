### MessageServiceImpl
**测试对象**：`src.main.java.com.demo.service.impl.MessageServiceImpl.java:findById(int messageID)`
**测试函数**：`test.java.com.demo.service.impl.MessageServiceImplTest.java:findById(int messageID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| 0 | 当存在对应ID时返回消息 | 返回的消息ID与输入一致 |    返回的消息ID与输入一致      |   正确   |
| 1 | 当ID无效时（如负数） | 抛出IllegalArgumentException |     不抛出IllegalArgumentException     |  错误    |

**测试对象**：`src.main.java.com.demo.service.impl.MessageServiceImpl.java:findByUser(String userID, Pageable pageable)`
**测试函数**：`test.java.com.demo.service.impl.MessageServiceImplTest.java:findByUser(String userID, Pageable pageable)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| 2 | 存在用户ID且分页参数有效 | 返回分页数据包含一条记录 |    返回分页数据包含一条记录      |  正确    |
| 3 | 分页参数为null | 方法正常执行，不抛出异常 |    方法正常执行，不抛出异常      |    正确  |
| 4 | 用户ID无效（空或null） | 抛出IllegalArgumentException |    不抛出IllegalArgumentException      |   错误   |

**测试对象**：`src.main.java.com.demo.service.impl.MessageServiceImpl.java:create(Message message)`
**测试函数**：`test.java.com.demo.service.impl.MessageServiceImplTest.java:create(Message message)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| 5 | 成功创建消息 | 返回生成的ID为1 |    返回生成的ID为1      |   正确   |
| 6 | 消息对象为null | 抛出NullPointerException |     抛出NullPointerException     |  正确    |

**测试对象**：`src.main.java.com.demo.service.impl.MessageServiceImpl.java:delById(int messageID)`
**测试函数**：`test.java.com.demo.service.impl.MessageServiceImplTest.java:delById(int messageID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| 7 | 删除有效ID的消息 | 调用删除方法无异常 |    调用删除方法无异常      |   正确   |
| 8 | 删除无效ID（如负数或0） | 抛出IllegalArgumentException |    不抛出IllegalArgumentException      |  错误    |

**测试对象**：`src.main.java.com.demo.service.impl.MessageServiceImpl.java:update(Message message)`
**测试函数**：`test.java.com.demo.service.impl.MessageServiceImplTest.java:update(Message message)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| 9 | 更新有效消息对象 | 调用保存方法无异常 |调用保存方法无异常           |正确      |
| 10 | 消息对象为null | 抛出IllegalArgumentException |抛出IllegalArgumentException           |错误      |

**测试对象**：`src.main.java.com.demo.service.impl.MessageServiceImpl.java:confirmMessage(int messageID)`
**测试函数**：`test.java.com.demo.service.impl.MessageServiceImplTest.java:confirmMessage(int messageID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| 11 | 确认存在的消息 | 消息状态更新为通过 |    消息状态更新为通过      |   正确   |
| 12 | 消息不存在时确认 | 抛出RuntimeException |   抛出RuntimeException       |  正确   |

**测试对象**：`src.main.java.com.demo.service.impl.MessageServiceImpl.java:rejectMessage(int messageID)`
**测试函数**：`test.java.com.demo.service.impl.MessageServiceImplTest.java:rejectMessage(int messageID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| 13 | 拒绝存在的消息 | 消息状态更新为拒绝 |消息状态更新为拒绝           |正确      |
| 14 | 消息不存在时拒绝 | 抛出RuntimeException |抛出RuntimeException           |正确      |

**测试对象**：`src.main.java.com.demo.service.impl.MessageServiceImpl.java:findWaitState(Pageable pageable)`
**测试函数**：`test.java.com.demo.service.impl.MessageServiceImplTest.java:findWaitState(Pageable pageable)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| 15 | 分页参数有效时查询待审核消息 | 返回分页数据包含一条记录 | 返回分页数据包含一条记录          | 正确      |
| 16 | 分页参数为null | 抛出IllegalArgumentException |不抛出IllegalArgumentException           |错误      |

**测试对象**：`src.main.java.com.demo.service.impl.MessageServiceImpl.java:findPassState(Pageable pageable)`
**测试函数**：`test.java.com.demo.service.impl.MessageServiceImplTest.java:findPassState(Pageable pageable)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| 17 | 分页参数有效时查询已通过消息 | 返回分页数据包含一条记录 | 返回分页数据包含一条记录          | 正确      |
| 18 | 分页参数为null | 抛出IllegalArgumentException |不抛出IllegalArgumentException           |错误      |

### MessageVoServiceImpl
**测试对象**：`src.main.java.com.demo.service.impl.MessageVoServiceImpl.java:returnMessageVoByMessageID(int messageID)`
**测试函数**：`test.java.com.demo.service.impl.MessageVoServiceImplTest.java:returnMessageVoByMessageID(int messageID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.MessageVoServiceImpl.java:returnVo(List<Message> messages)`
**测试函数**：`test.java.com.demo.service.impl.MessageVoServiceImplTest.java:returnVo(List<Message> messages)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



### NewsServiceImpl
**测试对象**：`src.main.java.com.demo.service.impl.NewsServiceImpl.java:findAll(Pageable pageable)`  
**测试函数**：`test.java.com.demo.service.impl.NewsServiceImplTest.java:findAll(Pageable pageable)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 分页参数有效时查询新闻 | 返回分页数据（包含一条记录） | 返回分页数据（包含一条记录）           |      正确|



**测试对象**：`src.main.java.com.demo.service.impl.NewsServiceImpl.java:findById(int newsID)`  
**测试函数**：`test.java.com.demo.service.impl.NewsServiceImplTest.java:findById(int newsID)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 通过有效ID查询新闻 | 返回对应的新闻对象 | 返回对应的新闻对象           |      正确|  
| 1 | 通过无效ID查询新闻 | 抛出EntityNotFoundException | 抛出EntityNotFoundException           |      正确|



**测试对象**：`src.main.java.com.demo.service.impl.NewsServiceImpl.java:create(News news)`  
**测试函数**：`test.java.com.demo.service.impl.NewsServiceImplTest.java:create(News news)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 创建有效新闻 | 返回生成的新闻ID（100） | 返回生成的新闻ID（100）           |      正确|  
| 1 | 创建缺少必填字段的新闻 | 抛出DataIntegrityViolationException | 抛出DataIntegrityViolationException           |      正确|  
| 2 | 传入null创建新闻 | 抛出IllegalArgumentException |   不抛出IllegalArgumentException       |    错误  |



**测试对象**：`src.main.java.com.demo.service.impl.NewsServiceImpl.java:delById(int newsID)`  
**测试函数**：`test.java.com.demo.service.impl.NewsServiceImplTest.java:delById(int newsID)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 删除有效ID的新闻 | 调用删除方法无异常 | 调用删除方法无异常           |      正确|  
| 1 | 删除无效ID的新闻 | 不抛出异常，正常执行 | 不抛出异常，正常执行           |      正确|



**测试对象**：`src.main.java.com.demo.service.impl.NewsServiceImpl.java:update(News news)`  
**测试函数**：`test.java.com.demo.service.impl.NewsServiceImplTest.java:update(News news)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 更新有效新闻 | 调用保存方法无异常 | 调用保存方法无异常           |      正确|  
| 1 | 传入null更新新闻 | 抛出IllegalArgumentException |    不抛出IllegalArgumentException      |   错误   |


### OrderServiceImpl
**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:findById(int OrderID)`
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:findById(int OrderID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:findDateOrder(int venueID, LocalDateTime startTime, LocalDateTime startTime2)`
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:findDateOrder(int venueID, LocalDateTime startTime, LocalDateTime startTime2)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:findUserOrder(String userID, Pageable pageable)`
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:findUserOrder(String userID, Pageable pageable)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:updateOrder(int orderID, String venueName, LocalDateTime startTime, int hours, String userID)`
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:updateOrder(int orderID, String venueName, LocalDateTime startTime, int hours, String userID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:submit(String venueName, LocalDateTime startTime, int hours, String userID)`
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:submit(String venueName, LocalDateTime startTime, int hours, String userID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:delOrder(int orderID)`
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:delOrder(int orderID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:confirmOrder(int orderID)`
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:confirmOrder(int orderID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:finishOrder(int orderID)`
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:finishOrder(int orderID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:rejectOrder(int orderID)`
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:rejectOrder(int orderID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:findNoAuditOrder(Pageable pageable)`
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:findNoAuditOrder(Pageable pageable)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:findAuditOrder()`
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:findAuditOrder()`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



### OrderVoServiceImpl
**测试对象**：`src.main.java.com.demo.service.impl.OrderVoServiceImpl.java:returnOrderVoByOrderID(int orderID)`
**测试函数**：`test.java.com.demo.service.impl.OrderVoServiceImplTest.java:returnOrderVoByOrderID(int orderID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.OrderVoServiceImpl.java:returnVo(List<Order> list)`
**测试函数**：`test.java.com.demo.service.impl.OrderVoServiceImplTest.java:returnVo(List<Order> list)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



### UserServiceImpl
**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:findByUserID(String userID)`
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:findByUserID(String userID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:findById(int id)`
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:findById(int id)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:findByUserID(Pageable pageable)`
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:findByUserID(Pageable pageable)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:checkLogin(String userID, String password)`
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:checkLogin(String userID, String password)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:create(User user)`
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:create(User user)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:delByID(int id)`
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:delByID(int id)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:updateUser(User user)`
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:updateUser(User user)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:countUserID(String userID)`
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:countUserID(String userID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|          |          |          |          |      |
|          |          |          |          |      |
|          |          |          |          |      |



### VenueServiceImpl
**测试对象**：`src.main.java.com.demo.service.impl.VenueServiceImpl.java:findByVenueID(int id)`  
**测试函数**：`test.java.com.demo.service.impl.VenueServiceImplTest.java:findByVenueID(int id)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 通过有效ID查询场馆 | 返回对应ID的场馆对象 |           返回对应ID的场馆对象 |正确      |  
| 1 | 通过无效ID查询场馆 | 抛出`EntityNotFoundException`异常 |           抛出`EntityNotFoundException`异常 |正确      |



**测试对象**：`src.main.java.com.demo.service.impl.VenueServiceImpl.java:findByVenueName(String venueName)`  
**测试函数**：`test.java.com.demo.service.impl.VenueServiceImplTest.java:findByVenueName(String venueName)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 名称完全匹配时查询场馆 | 返回对应名称的场馆对象 |           返回对应名称的场馆对象 |正确      |



**测试对象**：`src.main.java.com.demo.service.impl.VenueServiceImpl.java:findAll(Pageable pageable)`  
**测试函数**：`test.java.com.demo.service.impl.VenueServiceImplTest.java:findAll(Pageable pageable)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 分页参数有效时查询场馆 | 返回分页数据（包含一条记录） |           返回分页数据（包含一条记录） |正确      |



**测试对象**：`src.main.java.com.demo.service.impl.VenueServiceImpl.java:findAll()`  
**测试函数**：`test.java.com.demo.service.impl.VenueServiceImplTest.java:findAll()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 非分页查询所有场馆 | 返回场馆列表（包含一条记录） |           返回场馆列表（包含一条记录） |正确      |



**测试对象**：`src.main.java.com.demo.service.impl.VenueServiceImpl.java:create(Venue venue)`  
**测试函数**：`test.java.com.demo.service.impl.VenueServiceImplTest.java:create(Venue venue)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 创建有效场馆 | 返回生成的场馆ID（100） |           返回生成的场馆ID（100） |正确      |  
| 1 | 创建重复名称的场馆 | 抛出`DataIntegrityViolationException`异常 |           抛出`DataIntegrityViolationException`异常 |正确      |  
| 2 | 传入`null`创建场馆 | 抛出`IllegalArgumentException`异常 |不抛出`IllegalArgumentException`异常          |  错误    |



**测试对象**：`src.main.java.com.demo.service.impl.VenueServiceImpl.java:update(Venue venue)`  
**测试函数**：`test.java.com.demo.service.impl.VenueServiceImplTest.java:update(Venue venue)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 更新有效场馆 | 调用保存方法无异常 |           调用保存方法无异常 |正确      |  
| 1 | 传入`null`更新场馆 | 抛出`IllegalArgumentException`异常 |    不抛出`IllegalArgumentException`异常      |   错误   |



**测试对象**：`src.main.java.com.demo.service.impl.VenueServiceImpl.java:delById(int id)`  
**测试函数**：`test.java.com.demo.service.impl.VenueServiceImplTest.java:delById(int id)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 删除有效ID的场馆 | 调用删除方法无异常 |           调用删除方法无异常 |正确      |



**测试对象**：`src.main.java.com.demo.service.impl.VenueServiceImpl.java:countVenueName(String venueName)`  
**测试函数**：`test.java.com.demo.service.impl.VenueServiceImplTest.java:countVenueName(String venueName)`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |  
|:--------:|:--------:|:--------:|:--------:|:----:|  
| 0 | 统计存在匹配名称的场馆 | 返回匹配数量（2） |           返回匹配数量（2） |正确      |




