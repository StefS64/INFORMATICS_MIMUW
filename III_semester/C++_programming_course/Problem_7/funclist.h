#include <vector>
#include <string>
#include <functional>
#include <ranges>
#include <sstream>

namespace flist {

    namespace detail {
        auto _of_range(auto &r, auto it, auto f, auto a) {
            if (it == r.end()) {
                return a;
            } else {
                return f(*it, _of_range(r, std::next(it), f, a));
            }
        }
    }

    const auto empty = [](auto, auto a) {
            return a;
    };

    const auto cons = [](auto x, auto l) {
        return [=](auto f, auto a) {
            return f(x, l(f, a));
        };
    };

    const auto concat = [](auto l, auto k) {
        return [=](auto f, auto a) {
            return l(f, k(f, a));
        };
    };

    const auto rev = [](auto l) {
        return [=](auto f, auto a) {
            using A = decltype(a);
            using lambda_t = std::function<A(A)>;
            return l(
                [=](auto x, lambda_t g) {
                    return static_cast<lambda_t>([=](A a) {
                        return g(f(x, a));
                    });
                },
                static_cast<lambda_t>([=](A a) -> A {
                    return a;
                })
            )(a);
        };
    };

    const auto of_range = [](auto r) {
        decltype(auto) range = std::unwrap_reference_t<decltype(r)>(r);
        return [=](auto f, auto a) {
            auto it = range.begin();
            return (it == range.end()) ? a : f(*it, detail::_of_range(range, std::next(it), f, a));
        };
    };
    
    const auto create = [](auto... args) {
        return rev([=](auto f, auto a) {
            ((a = f(args, a)), ...);
            return a;
        });
    };

    const auto map = [](auto m, auto l) {
        return [=](auto f, auto a) {
            return l([=](auto x, auto b) {
                return f(m(x), b);
            }, a);
        };
    };

    const auto filter = [](auto p, auto l) {
        return [=](auto f, auto a) {
            return l([=](auto x, auto b) {
                if (p(x)) {
                    return f(x, b);
                }
                return b;
            }, a);
        };
    };

    const auto flatten = [](auto l) {
        return [=](auto f, auto a) {
            return l([=](auto g, auto b) {
                return g(f, b);
            }, a);
        };
    };

    const auto as_string = [](const auto& l) {
        std::ostringstream os;
        os << "[";
        bool first_elem = true;
        rev(l)([&](const auto& x, int) {
            if(!first_elem) {
                os << ";";
            }
            first_elem = false;
            os << x;
            return 0;
        }, 0);  
        os << "]";
        return os.str();
    };
} // namespace flist