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
|TC1	| 有效messageID和userID	| 正确返回MessageVo对象，字段映射正确	| 验证所有字段匹配	| 通过 |
|TC2	| 最小有效messageID(0)	| 抛出NullPointerException	| 触发异常并断言成功	| 通过 |
|TC3	| message存在但关联用户不存在	| 抛出NullPointerException	| 触发异常并断言成功|	通过 |
|TC4	| 输入不存在messageID	| 抛出NullPointerException	| 触发异常并断言成功|	通过 |



**测试对象**：`src.main.java.com.demo.service.impl.MessageVoServiceImpl.java:returnVo(List<Message> messages)`
**测试函数**：`test.java.com.demo.service.impl.MessageVoServiceImplTest.java:returnVo(List<Message> messages)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|   TC1       |   空列表输入       |     返回空列表     |    assertTrue(result.isEmpty()) 通过      |  通过    |
| TC2	|单个元素列表	|返回包含1个正确MessageVo的列表|	字段验证通过|	通过|
| TC3	|混合有效和无效元素的列表	|抛出NullPointerException	|触发异常并断言成功	|通过|
| TC4	|重复元素列表	|返回包含2个元素的列表|	assertEquals(2, result.size()) 通过	|通过|



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

**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:submit(String venueName, LocalDateTime startTime, int hours, String userID)`  
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:submit_ValidOrder_SavesSuccessfully()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 创建有效订单 | 成功保存订单并计算金额 | 验证状态、金额、用户ID和场馆ID正确性 | 通过 |
| TC2 | 创建0小时订单 | 抛出参数异常 | 捕获IllegalArgumentException，验证错误信息 | 错误 |

---

**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.javaOrder(int orderID, String venueName, LocalDateTime startTime, int hours, String userID)`  
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:updateOrder_ChangeVenue_RecalculatesTotal()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 更新场馆信息 | 重新计算订单金额 | 验证新场馆ID、金额和状态重置为待审核 | 通过 |
| TC2 | 更新为相同时间 | 无变化 | 验证时间未修改且保存操作触发 | 通过 |

---

**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:confirmOrder(int orderID)`  
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:confirmOrder_PendingOrder_UpdatesState()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 审核待处理订单 | 状态变更为等待中 | 验证DAO层状态更新调用 | 通过 |

---

**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:finishOrder(int orderID)`  
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:finishOrder_ApprovedOrder_Success()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 完成已审核订单 | 状态变更为已完成 | 验证状态更新操作无异常 | 通过 |

---

**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:rejectOrder(int orderID)`  
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:rejectOrder_NonExistentOrder_ThrowsException()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 拒绝不存在订单 | 抛出运行时异常 | 捕获异常并验证错误信息 | 通过 |

---

**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:findUserOrder(String userID, Pageable pageable)`  
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:findUserOrder_Pagination_ReturnsData()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 分页查询用户订单 | 返回匹配的分页数据 | 验证元素数量及用户ID一致性 | 通过 |
| TC2 | 查询时间段无订单 | 返回空列表 | assertTrue验证结果为空 | 通过 |

---

**测试对象**：`src.main.java.com.demo.service.impl.OrderServiceImpl.java:findDateOrder(int venueID, LocalDateTime start, LocalDateTime end)`  
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:findDateOrder_NoBookings_ReturnsEmpty()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 查询无预约时间段 | 返回空列表 | assertTrue验证结果为空 | 通过 |

---

**测试对象**：.main.java.com.demo.service.impl.OrderServiceImpl.java:delOrder(int orderID)`  
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:delOrder_ValidOrder_DeletesSuccessfully()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 删除有效订单 | 无异常抛出 | assertDoesNotThrow验证删除操作 | 通过 |

---

**测试对象**：边界条件  
**测试函数**：`test.java.com.demo.service.impl.OrderServiceImplTest.java:submit_MinimumDuration_Success()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 创建最小时长订单（1小时） | 计算最小金额 | 验证总金额为200 | 通过 |

### OrderVoServiceImpl
**测试对象**：`src.main.java.com.demo.service.impl.OrderVoServiceImpl.java:returnOrderVoByOrderID(int orderID)`
**测试函数**：`test.java.com.demo.service.impl.OrderVoServiceImplTest.java:returnOrderVoByOrderID(int orderID)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|TC1	|有效订单ID查询	|返回包含正确场馆名称的OrderVo|	验证字段映射成功|	通过|
|TC2	|无效订单ID查询	|抛出RuntimeException|	触发异常并断言成功	|通过|



**测试对象**：`src.main.java.com.demo.service.impl.OrderVoServiceImpl.java:returnVo(List<Order> list)`
**测试函数**：`test.java.com.demo.service.impl.OrderVoServiceImplTest.java:returnVo(List<Order> list)`
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
|TC1	|有效订单列表转换	|返回正确数量的OrderVo列表	|验证列表长度和字段映射	|通过|


### UserServiceImpl
**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:findByUserID(String userID)`  
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:findByUserID_ValidID_ReturnsUser()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 有效用户ID查询 | 返回匹配的用户对象 | 验证用户ID、姓名、邮箱字段正确性 | 通过 |
| TC2 | 不存在的用户ID查询 | 返回null | assertNull 通过 | 通过 |

---

**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:findByUserID(Pageable pageable)`  
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:findByUserID_PageQuery_ReturnsPagedResults()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 分页查询普通用户 | 返回分页数据 | 验证总元素数、非空集合及首条数据 | 通过 |
| TC2 | 空结果分页查询 | 返回空页 | assertTrue(result.isEmpty()) 通过 | 通过 |

---

**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:checkLogin(String userID, String password)`  
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:checkLogin_ValidCredentials_ReturnsUser()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 正确凭证验证 | 返回用户对象 | assertEquals 对象匹配验证 | 通过 |
| TC2 | 错误密码验证 | 返回null | assertNull 通过 | 通过 |

---

**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:create(User user)`  
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:create_NewUser_ReturnsUserCount()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 创建新用户 | 返回当前用户总数 | 验证总数增量及用户字段保存正确性 | 通过 |

---

**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:delByID(int id)`  
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:delByID_ExistingUser_DeletesSuccessfully()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 删除存在的用户 | 无异常抛出 | assertDoesNotThrow 通过 | 通过 |

---

**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:updateUser(User user)`  
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:updateUser_ValidData_SavesUpdatedUser()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 更新用户信息 | 保存修改后的数据 | 验证用户名、邮箱、密码字段更新 | 通过 |
| TC2 | 更新不存在用户 | 静默保存新用户 | verify DAO保存操作触发 | 通过 |

---

**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:countUserID(String userID)`  
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:countUserID_ExistingID_ReturnsOne()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 统计存在的用户ID | 返回计数1 | assertEquals(1, count) | 通过 |
| TC2 | 统计ID | 返回计数0 | assertEquals(0, count) | 通过 |

---

**异常场景测试**  
**测试对象**：`src.main.java.com.demo.service.impl.UserServiceImpl.java:create(User user)`  
**测试函数**：`test.java.com.demo.service.impl.UserServiceImplTest.java:create_DuplicateUserID_ReturnsTotalCount()`  
| 用例编号 | 用例描述 | 预期结果 | 测试结果 | 结论 |
|:--------:|:--------:|:--------:|:--------:|:----:|
| TC1 | 创建重复用户ID | 抛出运行时异常 | assertThrows 验证异常类型 | 错误 |


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




