#include <iostream>
#include <sstream>
#include <string>
#include <functional>


namespace flist
{
    auto empty = [](auto f, auto a) {return a;}; 

    namespace detail {
        auto _of_range(auto &r, auto it, auto f, auto a) {
            if (it == r.end()) {
                return a;
            } else {
                return f(*it, _of_range(r, std::next(it), f, a));
            }
        }
    }
   

    auto cons = [](auto x, auto l) {  
        return [=](auto f, auto a) {
            return f(x, l(f, a));
        };
    };

    auto create(auto... args) {
        return [=] (auto f, auto a) {
            return (..., f(args, a));
        };
    };

    auto of_range(auto r) {
        return [=](auto f, auto a) {
            auto it = r.begin();
            return (it == r.end()) ? a : f(*it, detail::_of_range(r, std::next(it), f, a));
        };
    };

    std::string as_string(const auto& l) {
        
        std::ostringstream os;
        auto f = [&](auto x, auto a) {
            os << x << " ";
            return a;
        };
        l(f, NULL);
        return os.str();
    }
    
} // namespace flist
