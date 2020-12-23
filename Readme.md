### Features
https://github.com/tkaczmarzyk/specification-arg-resolver
- Dynamic Generated Spec for JPA PagingAndSortingRepository, JpaSpecificationExecutor Interface


# Setup
##gradle
```
compile group: 'net.kaczmarzyk', name: 'specification-arg-resolver', version: '2.6.2'
```

##add config
```
@Configuration
@EnableJpaRepositories
public class SpecConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SpecificationArgumentResolver());
    }
}

```
###Speical Compare Class Override default status check
due to the short usage of path search cannot be setup with ordrers
e.g. status default should be != "Deleted"

make sure this enhenced Equal Compare are saved in core
```
public class BitEqual<T> extends Equal<T> {

    private Converter converter;

    public BitEqual(QueryContext queryContext, String path, String[] httpParamValues, Converter converter) {
        super(queryContext, path, httpParamValues, converter);
        this.converter = converter;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Class<?> typeOnPath = path(root).getJavaType();
        if(expectedValue.charAt(0) == '!'){
            expectedValue = expectedValue.substring(1, expectedValue.length());
            return cb.notEqual(path(root), this.converter.convert(expectedValue, typeOnPath));
        }
        else{
            return cb.equal(path(root), this.converter.convert(expectedValue, typeOnPath));
        }
    }
}
```
## use jpa default pagination setup

```
spring.data.web.pageable.default-page-size=20
spring.data.web.pageable.max-page-size=10000
spring.data.web.pageable.one-indexed-parameters=true
spring.data.web.pageable.page-parameter=page
spring.data.web.pageable.prefix=
#Delimiter to be used between the qualifier and the actual page number and size properties.
#spring.data.web.pageable.qualifier-delimiter=
spring.data.web.pageable.size-parameter=size # Page size parameter name.
spring.data.web.sort.sort-parameter=sort # Sort parameter name.
```

controller can be rewrite to
```
@GetMapping("/courses")
public ResponseEntity<CoreResponseBody> searchByLabel(
        @Spec(path = "label", spec = Like.class) Specification<Course> spec,
        Pageable pageable
) {
    Page<Course> courses = courseRepository.findAll(spec, pageable);
    return ResponseEntity.ok(CoreResponseBody.result(courses));
}
```


## Usage see controller for detail how annotation worked
- Basic Search http://localhost:8080/courses?label=test
- Search with pagination and sort http://localhost:8080/courses?page=10&sort=label,desc&size=1&label=1  
- Default Status Active Only http://localhost:8080/coursesStatus or search deleted only
  http://localhost:8080/coursesStatus?status=Deleted
- Search Date Range http://localhost:8080/searchDateRange?startDateBefore=2020-12-23&startDateAfter=2020-12-22
- Search with child table http://localhost:8080/joniSearch2?course.label=test2 (need bidirectional setup)
------------






