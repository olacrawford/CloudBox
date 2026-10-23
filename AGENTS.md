# AGENTS.md — CloudBox

## What this is

CloudBox is a personal cloud-storage (网盘) backend: accounts, file management with chunked upload and storage quota, and file sharing. Early stage — only the base infrastructure (response wrapper, error codes, exception handling, utils) exists; no controllers/services/mappers yet.

- **Stack**: Java 21, Spring Boot 3.2.4 (parent), Maven 3.10.0 via wrapper (`./mvnw`)
- **Root package**: `com.ibupro.cloudbox` (`src/main/java/com/ibupro/cloudbox/`)
- **Planned features** (visible in `BizCodeEnum`): account register/login, file CRUD + chunk upload + quota, share links with codes/expiry
- **Key dependencies**: MyBatis-Plus 3.5.6 (+ generator with Velocity templates), MySQL 8, MinIO 8.3.7 and AWS S3 SDK (object storage), jjwt 0.12.3 (auth), Hutool, Fastjson 2, knife4j 4.4.0 (OpenAPI docs), Lombok

## Commands

```bash
./mvnw compile              # compile
./mvnw spring-boot:run      # run (listens on port 8081)
./mvnw package              # build jar
./mvnw test -DskipTests=false   # run tests
```

**Gotcha**: the surefire plugin has `<skipTests>true</skipTests>` in `pom.xml`, so `./mvnw test` and `./mvnw package` silently skip all tests. Pass `-DskipTests=false` to actually run them.

## Conventions (established in existing code — follow them)

- **Uniform response**: every endpoint returns `JsonData` (`util/JsonData.java`) with `{code, data, msg}`. `code == 0` means success; use the static builders (`buildSuccess`, `buildError`, `buildResult`). Never return raw entities or ad-hoc maps.
- **Errors**: throw `BizException(BizCodeEnum.X)` from anywhere; the global `@ControllerAdvice` (`exception/CustomExceptionHandler.java`) converts it to a `JsonData` error. Non-biz exceptions become a generic "系统异常". Do not try/catch into your own response shapes.
- **Error codes**: add new codes to `enums/BizCodeEnum.java`, never inline numeric codes. Existing ranges: 2500xx accounts, 2204xx/2304xx/2404xx files, 2604xx shares — pick a new 6-digit code consistent with the domain prefix.
- **Lombok**: use `@Data`, `@Builder`, `@Slf4j` etc. instead of hand-writing getters/loggers; logging goes through `@Slf4j`'s `log`.
- **Language**: Javadoc, comments, and user-facing error messages are written in Chinese. Keep that style.

## Things to watch

- Two JSON libraries coexist: `JsonData.getData()` uses Fastjson; `util/JsonUtil.java` wraps Jackson (with a configured shared `ObjectMapper`). Prefer `JsonUtil` for new code unless matching existing Fastjson usage.
- `application.yml` only sets port 8081 and app name. There is no datasource/MinIO/JWT config yet — any feature touching MyBatis-Plus or object storage needs its config added there (and none of those beans will start without it).
- `mybatis-plus-generator` + Velocity are on the classpath for code generation; don't ship generated scaffolding into `src` without the user asking.
- Spring Boot dependency versions are inconsistently pinned in `pom.xml` (web 3.3.4, aop 3.0.5, test 3.3.1 override the 3.2.4 parent). Don't "fix" versions ad hoc; follow the existing property-based style (`<mybatisplus.version>` etc.) when adding new ones.
