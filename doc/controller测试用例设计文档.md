## 二、集成测试用例

### 1. 管理员用户管理模块测试

- **测试对象**: `src.main.java.com.demo.controller.admin.AdminUserController.java`
- **测试脚本**: `src.test.java.com.demo.controller.admin.AdminUserControllerTest.java`

#### 功能点列表

| 功能点 | 用例编号 | 用例描述 | 测试结果 | 预计结果 | 结论 |
| ------- | -------- | -------- | -------- | -------- | ---- |
| 用户管理列表 | TAU-001 | 用户管理列表首页分页显示 | 返回用户管理页面，并包含总页数 | 返回用户管理页面，并包含总页数 | 通过 |
| 用户列表查询 | TAU-002 | 用户列表分页参数边界值测试（正常页码） | 返回用户列表 | 返回用户列表 | 通过 |
| 用户列表查询 | TAU-003 | 用户列表分页参数边界值测试（边界页码） | 返回对应页码的用户列表 | 返回对应页码的用户列表 | 通过 |
| 用户添加页面 | TAU-004 | 用户添加页面获取 | 返回"admin/user_add"视图 | 返回"admin/user_add"视图 | 通过 |
| 用户添加 | TAU-005 | 正常添加用户 | 编译错误：未解决的编译问题 | 用户被成功添加并重定向到用户管理页面 | 失败 |
| 用户编辑页面 | TAU-006 | 获取用户编辑页面 | 返回"admin/user_edit"视图，并包含用户信息 | 返回"admin/user_edit"视图，并包含用户信息 | 通过 |
| 用户修改 | TAU-007 | 修改用户信息 | 用户信息被成功修改并重定向到用户管理页面 | 用户信息被成功修改并重定向到用户管理页面 | 通过 |
| 用户ID检查 | TAU-008 | 检查用户ID是否存在（不存在的ID） | 返回true（可用） | 返回true（可用） | 通过 |
| 用户ID检查 | TAU-009 | 检查用户ID是否存在（已存在的ID） | 返回false（不可用） | 返回false（不可用） | 通过 |
| 用户删除 | TAU-010 | 删除存在的用户 | 用户被成功删除并返回true | 用户被成功删除并返回true | 通过 |
| 用户删除 | TAU-011 | 删除不存在的用户 | 抛出异常 | 抛出异常 | 通过 |

### 2. 管理员订单管理模块测试

- **测试对象**: `src.main.java.com.demo.controller.admin.AdminOrderController.java`
- **测试脚本**: `src.test.java.com.demo.controller.admin.AdminOrderControllerTest.java`

#### 功能点列表

| 功能点 | 用例编号 | 用例描述 | 测试结果 | 预计结果 | 结论 |
| ------- | -------- | -------- | -------- | -------- | ---- |
| 订单管理页面 | TAO-001 | 订单管理页面正常加载（有订单数据） | 页面加载并显示订单列表和总页数 | 页面加载并显示订单列表和总页数 | 通过 |
| 订单管理页面 | TAO-002 | 订单管理页面正常加载（无订单数据） | 页面加载但订单列表为空 | 页面加载但订单列表为空 | 通过 |
| 未审核订单查询 | TAO-003 | 查询未审核订单（第一页） | 返回第一页订单数据 | 返回第一页订单数据 | 通过 |
| 未审核订单查询 | TAO-004 | 查询未审核订单（第二页） | 返回第二页订单数据 | 返回第二页订单数据 | 通过 |
| 未审核订单查询 | TAO-005 | 查询未审核订单（负页码） | 嵌套Servlet异常 | 返回空列表 | 失败 |
| 未审核订单查询 | TAO-006 | 查询未审核订单（超出范围页码） | 返回空列表 | 返回空列表 | 通过 |
| 订单审核通过 | TAO-007 | 通过订单（有效ID） | 返回true表示订单已通过 | 返回true表示订单已通过 | 通过 |
| 订单审核通过 | TAO-008 | 通过订单（无效ID） | 返回true（即使ID无效） | 返回true（即使ID无效） | 通过 |
| 订单审核拒绝 | TAO-009 | 拒绝订单（有效ID） | 返回true表示订单已拒绝 | 返回true表示订单已拒绝 | 通过 |
| 订单审核拒绝 | TAO-010 | 拒绝订单（无效ID） | 返回true（即使ID无效） | 返回true（即使ID无效） | 通过 |

### 3. 管理员留言管理模块测试
- **测试对象**: `src.main.java.com.demo.controller.admin.AdminMessageController.java`
- **测试脚本**: `src.test.java.com.demo.controller.admin.AdminMessageControllerTest.java`

#### 功能点列表

| 功能点 | 用例编号 | 用例描述 | 测试结果 | 预计结果 | 结论 |
| ------- | -------- | -------- | -------- | -------- | ---- |
| 留言管理页面 | TAM-001 | 留言管理页面加载 | 页面加载并显示总页数 | 页面加载并显示总页数 | 通过 |
| 留言列表查询 | TAM-002 | 查询留言列表（正常页码） | 返回留言列表 | 返回留言列表 | 通过 |
| 留言列表查询 | TAM-003 | 查询留言列表（负页码） | 嵌套Servlet异常 | 返回空列表 | 失败 |
| 留言列表查询 | TAM-004 | 查询留言列表（超出范围页码） | 返回空列表 | 返回空列表 | 通过 |
| 留言审核 | TAM-005 | 审核通过留言（有效ID） | 返回true | 返回true | 通过 |
| 留言审核 | TAM-006 | 审核不通过留言（无效ID） | 返回true | 返回true | 通过 |
| 留言删除 | TAM-007 | 删除留言 | 返回true | 返回true | 通过 |

### 4. 管理员新闻管理模块测试

- **测试对象**: `src.main.java.com.demo.controller.admin.AdminNewsController.java`
- **测试脚本**: `src.test.java.com.demo.controller.admin.AdminNewsControllerTest.java`

#### 功能点列表

| 功能点 | 用例编号 | 用例描述 | 测试结果 | 预计结果 | 结论 |
| ------- | -------- | -------- | -------- | -------- | ---- |
| 新闻管理页面 | TAN-001 | 新闻管理页面加载 | 页面加载并显示总页数 | 页面加载并显示总页数 | 通过 |
| 新闻列表查询 | TAN-002 | 查询新闻列表（正常页码） | 返回新闻列表 | 返回新闻列表 | 通过 |
| 新闻列表查询 | TAN-003 | 查询新闻列表（负页码） | 嵌套Servlet异常 | 返回空列表 | 失败 |
| 新闻列表查询 | TAN-004 | 查询新闻列表（超出范围页码） | 返回空列表 | 返回空列表 | 通过 |
| 新闻添加页面 | TAN-005 | 新闻添加页面加载 | 返回新闻添加页面 | 返回新闻添加页面 | 通过 |
| 新闻编辑页面 | TAN-006 | 获取编辑页面（有效ID） | 返回编辑页面并包含新闻数据 | 返回编辑页面并包含新闻数据 | 通过 |
| 新闻编辑页面 | TAN-007 | 获取编辑页面（无效ID） | 嵌套Servlet异常 | 返回编辑页面 | 失败 |
| 新闻删除 | TAN-008 | 删除新闻（有效ID） | 返回true | 返回true | 通过 |
| 新闻编辑 | TAN-009 | 编辑新闻 | 编辑成功并重定向到新闻管理页面 | 编辑成功并重定向到新闻管理页面 | 通过 |
| 新闻添加 | TAN-010 | 添加新闻（有效数据） | 添加成功并重定向到新闻管理页面 | 添加成功并重定向到新闻管理页面 | 通过 |
| 新闻添加 | TAN-011 | 添加新闻（空标题） | 重定向到新闻管理页面 | 重定向到新闻管理页面 | 通过 |

### 5. 管理员场馆管理模块测试

- **测试对象**: `src.main.java.com.demo.controller.admin.AdminVenueController.java`
- **测试脚本**: `src.test.java.com.demo.controller.admin.AdminVenueControllerTest.java`

#### 功能点列表

| 功能点 | 用例编号 | 用例描述 | 测试结果 | 预计结果 | 结论 |
| ------- | -------- | -------- | -------- | -------- | ---- |
| 场馆管理页面 | TAV-001 | 场馆管理页面加载（有数据） | 页面加载并显示场馆列表和总页数 | 页面加载并显示场馆列表和总页数 | 通过 |
| 场馆管理页面 | TAV-002 | 场馆管理页面加载（无数据） | 模型属性'total'期望为0但实际为1 | 页面加载但场馆列表为空，总页数为0 | 失败 |
| 场馆编辑页面 | TAV-003 | 获取编辑页面（有效ID） | 返回编辑页面并包含场馆数据 | 返回编辑页面并包含场馆数据 | 通过 |
| 场馆编辑页面 | TAV-004 | 获取编辑页面（无效ID） | 模型属性'venue'不存在 | 返回编辑页面 | 失败 |
| 场馆添加页面 | TAV-005 | 场馆添加页面加载 | 返回场馆添加页面 | 返回场馆添加页面 | 通过 |
| 场馆修改 | TAV-006 | 修改场馆（有效数据） | 修改成功并重定向到场馆管理页面 | 修改成功并重定向到场馆管理页面 | 通过 |
| 场馆修改 | TAV-007 | 修改场馆（空图片） | 保留原图片并重定向到场馆管理页面 | 保留原图片并重定向到场馆管理页面 | 通过 |
| 场馆列表查询 | TAV-008 | 获取场馆列表 | 返回场馆列表数据 | 返回场馆列表数据 | 通过 |
| 场馆添加 | TAV-009 | 添加场馆（有效数据） | 添加成功并重定向到场馆管理页面 | 添加成功并重定向到场馆管理页面 | 通过 |
| 场馆添加 | TAV-010 | 添加场馆（无效数据） | 添加失败并重定向到场馆添加页面 | 添加失败并重定向到场馆添加页面 | 通过 |
| 场馆名称检查 | TAV-011 | 检查场馆名称（已存在） | 返回false | 返回false | 通过 |
| 场馆名称检查 | TAV-012 | 检查场馆名称（不存在） | 返回true | 返回true | 通过 |
| 场馆删除 | TAV-013 | 删除场馆（有效ID） | 返回true | 返回true | 通过 |
| 场馆删除 | TAV-014 | 删除场馆（无效ID） | 抛出异常 | 抛出异常 | 通过 |

### 6.用户留言管理模块测试

- **测试对象**: `src.main.java.com.demo.controller.user.MessageController.java`
- **测试脚本**: `src.test.java.com.demo.controller.user.MessageControllerTest.java`

#### 功能点列表

| 功能点 | 用例编号    | 用例描述 | 测试结果 | 预计结果 | 结论 |
| ------- |---------| -------- | -------- | -------- | ---- |
| 消息列表查询     | TUM-001 | 查询消息列表                          | 返回 JSON 数组   | 返回 JSON 数组   | 通过 |
| 发送消息         | TUM-002 | 发送消息                              | 重定向到消息列表 | 重定向到消息列表 | 通过 |
| 修改消息         | TUM-003 | 修改消息（存在的消息）                | 返回 true        | 返回 true        | 通过 |
| 删除消息         | TUM-004 | 删除消息                              | 返回 true        | 返回 true        | 通过 |
| 修改消息         | TUM-005 | 修改消息（不存在的消息）              | NullPointerException    | 返回 4xx 错误    | 失败 |
| 用户消息查询异常 | TUM-006 | 查询用户消息列表时触发异常            | 抛出 LoginException | 抛出 LoginException | 通过 |

### 7. 用户新闻管理模块测试

- **测试对象**: `src.main.java.com.demo.controller.user.NewsController.java`
- **测试脚本**: `src.test.java.com.demo.controller.user.NewsControllerTest.java`

#### 功能点列表

| 功能点 | 用例编号 | 用例描述 | 测试结果 | 预计结果 | 结论 |
| ------- | -------- | -------- | -------- | -------- | ---- |
| 新闻详情查询     | TUN-001 | 查询新闻详情（存在的新闻）            | 返回视图 "news" 并包含新闻数据          | 返回视图 "news" 并包含新闻数据 | 通过 |
| 新闻列表查询     | TUN-002 | 查询新闻列表                          | 返回 JSON 对象，包含新闻分页数据          | 返回 JSON 对象，包含新闻分页数据 | 通过 |
| 新闻列表视图     | TUN-003 | 查询新闻列表视图                      | 返回视图 "news_list" 并包含新闻列表和总页数 | 返回视图 "news_list" 并包含新闻列表和总页数 | 通过 |
| 新闻详情查询     | TUN-004 | 查询新闻详情（不存在的新闻）          | 渲染失败                         | 模型中 news 为空 | 失败 |

### 8. 用户订单管理模块测试

- **测试对象**: `src.main.java.com.demo.controller.user.OrderController.java`
- **测试脚本**: `src.test.java.com.demo.controller.user.OrderControllerTest.java`

#### 功能点列表

| 功能点 | 用例编号 | 用例描述 | 测试结果 | 预计结果 | 结论 |
| ------- | -------- | -------- | -------- | -------- |----|
| 订单管理页面         | TUO-001     | 访问订单管理页面（用户已登录）             | 返回视图 "order_manage" 并包含 total 属性 | 返回视图 "order_manage" 并包含 total 属性 | 通过 |
| 订单管理页面         | TUO-002     | 访问订单管理页面（用户未登录）             | 抛出 LoginException  | 抛出 LoginException  | 通过 |
| 场馆订单页面         | TUO-003     | 访问场馆订单页面（正常情况）               | 返回视图 "order_place" 并包含 venue 属性 | 返回视图 "order_place" 并包含 venue 属性 | 通过 |
| 场馆订单页面         | TUO-004     | 访问场馆订单页面（异常情况）               | 抛出 RuntimeException | 抛出 RuntimeException | 通过 |
| 订单下单页面         | TUO-005     | 访问订单下单页面（无参数）                 | 返回视图 "order_place" | 返回视图 "order_place" | 通过 |
| 获取订单列表         | TUO-006     | 获取订单列表（用户已登录）                 | 返回 JSON 格式的订单列表 | 返回 JSON 格式的订单列表 | 通过 |
| 获取订单列表         | TUO-007     | 获取订单列表（用户未登录）                 | 抛出 LoginException  | 抛出 LoginException  | 通过 |
| 提交订单             | TUO-008     | 提交订单（用户已登录，参数正确）           | 重定向至 order_manage 页面 | 重定向至 order_manage 页面 | 通过 |
| 提交订单             | TUO-009     | 提交订单（用户未登录）                     | 抛出 LoginException  | 抛出 DateTimeParseException  | 失败 |
| 完成订单             | TUO-010     | 完成订单（正常情况）                       | 返回 HTTP 200        | 返回 HTTP 200        | 通过 |
| 完成订单             | TUO-011     | 完成订单（异常情况）                       | 抛出 RuntimeException | 抛出 RuntimeException | 通过 |
| 编辑订单页面         | TUO-012     | 编辑订单页面（订单存在）                   | 返回视图 "order_edit" 并包含 order 和 venue 属性 | 返回视图 "order_edit" 并包含 order 和 venue 属性 | 通过 |
| 编辑订单页面         | TUO-013     | 编辑订单页面（订单不存在）                 | 抛出 NullPointerException | 抛出 NullPointerException | 通过 |
| 修改订单             | TUO-014     | 修改订单（用户已登录，参数正确）           | 返回 true 并重定向至 order_manage | 返回 true 并重定向至 order_manage | 通过 |
| 修改订单             | TUO-015     | 修改订单（用户未登录）                     | 抛出 LoginException  | 抛出 DateTimeParseException  | 失败 |
| 删除订单             | TUO-016     | 删除订单（正常情况）                       | 返回 "true"          | 返回 "true"          | 通过 |
| 删除订单             | TUO-017     | 删除订单（异常情况）                       | 抛出 RuntimeException | 抛出 RuntimeException | 通过 |
| 获取场馆订单信息     | TUO-018     | 获取场馆订单信息（正常情况）               | 返回 VenueOrder 对象（JSON 格式） | 返回 VenueOrder 对象（JSON 格式） | 通过 |
| 获取场馆订单信息     | TUO-019     | 获取场馆订单信息（异常情况）               | 抛出 NullPointerException | 抛出 NullPointerException | 通过 |

### 9. 用户用户管理模块测试

- **测试对象**: `src.main.java.com.demo.controller.user.UserController.java`
- **测试脚本**: `src.test.java.com.demo.controller.user.UserControllerTest.java`

#### 功能点列表

| 功能点      | 用例编号 | 用例描述 | 测试结果 | 预计结果 | 结论 |
|----------| -------- | -------- | -------- | -------- | ---- |
| 注册页面访问   | TUU-001     | 访问注册页面                               | 返回视图 "signup"    | 返回视图 "signup"    | 通过   |
| 登录页面访问   | TUU-002     | 访问登录页面                               | 返回视图 "login"     | 返回视图 "login"     | 通过   |
| 用户登录     | TUU-003     | 用户登录（普通用户，正确凭据）             | 返回 "/index" 并设置 session | 返回 "/index" 并设置 session | 通过   |
| 用户登录     | TUU-004     | 用户登录（管理员，正确凭据）               | 返回 "/admin_index" 并设置 session | 返回 "/admin_index" 并设置 session | 通过   |
| 用户登录     | TUU-005     | 用户登录（错误凭据）                       | 返回 "false"         | 返回 "false"         | 通过   |
| 用户注册     | TUU-006     | 用户注册（正常情况）                       | 重定向到 "login" 页面 | 重定向到 "login" 页面 | 通过   |
| 用户注册     | TUU-007     | 用户注册（异常情况）                       | 抛出 RuntimeException | 抛出 RuntimeException | 通过   |
| 用户登出     | TUU-008     | 用户登出                                   | 重定向到 "/index" 并移除 session | 重定向到 "/index" 并移除 session | 通过   |
| 管理员退出    | TUU-009     | 管理员退出                                 | 重定向到 "/index" 并移除 session | 重定向到 "/index" 并移除 session | 通过   |
| 更新用户信息   | TUU-010     | 更新用户信息（正常情况）                   | 重定向到 "user_info" 并更新 session | 重定向到 "user_info" 并更新 session | 通过   |
| 更新用户信息   | TUU-011     | 更新用户信息（用户不存在）                 | 抛出 NullPointerException | 抛出 NullPointerException | 通过   |
| 校验密码     | TUU-012     | 校验密码（匹配）                           | 返回 "true"          | 返回 "true"          | 通过   |
| 校验密码     | TUU-013     | 校验密码（不匹配）                         | 返回 "false"         | 返回 "false"         | 通过   |
| 用户信息页面访问 | TUU-014     | 访问用户信息页面                           | 返回视图 "user_info" | 返回视图 "user_info" | 通过   |

### 10. 用户场馆管理模块测试
- **测试对象**: `src.main.java.com.demo.controller.user.VenueController.java`
- **测试脚本**: `src.test.java.com.demo.controller.user.VenueControllerTest.java`

#### 功能点列表

| 功能点 | 用例编号 | 用例描述 | 测试结果 | 预计结果 | 结论 |
| ------- | -------- | -------- | -------- | -------- | ---- |
| 场馆详情页面         | TUV-001     | 访问场馆详情页面（正常情况）               | 返回视图 "venue" 并包含 venue 属性 | 返回视图 "venue" 并包含 venue 属性 | 通过   |
| 场馆详情页面         | TUV-002     | 访问场馆详情页面（异常情况）               | 抛出 RuntimeException | 抛出 RuntimeException | 通过   |
| 分页查看场馆         | TUV-003     | 分页查看场馆（正常情况）                   | 返回 JSON 格式的场馆分页数据 | 返回 JSON 格式的场馆分页数据 | 通过   |
| 分页查看场馆         | TUV-004     | 分页查看场馆（异常情况）                   | 抛出 RuntimeException | 抛出 RuntimeException | 通过   |
| 场馆列表页面         | TUV-005     | 查看场馆列表页面（正常情况）               | 返回视图 "venue_list" 并包含 venue_list 和 total 属性 | 返回视图 "venue_list" 并包含 venue_list 和 total 属性 | 通过   |
| 场馆列表页面         | TUV-006     | 查看场馆列表页面（异常情况）               | 抛出 RuntimeException | 抛出 RuntimeException | 通过   |

### 11. 首页模块测试

- **测试对象**: `src.main.java.com.demo.controller.IndexController.java`
- **测试脚本**: `src.test.java.com.demo.controller.IndexControllerTest.java`

#### 功能点列表

| 功能点 | 用例编号 | 用例描述 | 测试结果 | 预计结果 | 结论 |
| ------- | -------- | -------- | -------- | -------- | ---- |
| 首页             | TI-001      | 首页（正常情况）                           | 返回视图 "index"，并包含 "news_list"、"venue_list"、"message_list" 和 "user" 属性 | 返回视图 "index"，并包含 "news_list"、"venue_list"、"message_list" 和 "user" 属性 | 通过   |
| 管理员首页       | TI-002      | 管理员首页（正常情况）                     | 返回视图 "admin/admin_index" | 返回视图 "admin/admin_index" | 通过   |
| 管理员首页       | TI-003      | 管理员首页（异常情况）                     | 抛出 RuntimeException | 抛出 RuntimeException | 通过   |